package com.zone._blog.users;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.exceptions.BadRequestException;
import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.media.MediaMapper;
import com.zone._blog.media.MediaService;
import com.zone._blog.media.dto.MediaDto;
import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MediaService mediaService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mediaService = mediaService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserInfo user = this.userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(login, login).orElseThrow(
                () -> new UsernameNotFoundException("User " + login + " not found"));

        return user;
    }

    // private static PostCreator getPostCreator() {
    //     var auth = (JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
    //     var claims = auth.getToken().getClaims();
    //     var userId = (String) claims.get("id");
    //     var userName = (String) claims.get("name");
    //     PostCreator creator = PostCreator.builder()
    //             .id(userId)
    //             .name(userName)
    //             .build();
    //     return creator;
    // }
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication ==========> " + authentication.toString());
        UserInfo currentUser = (UserInfo) authentication.getPrincipal();
        return UserMapper.toUserResponse(currentUser);
    }

    @Transactional
    @Override
    public UserResponse addUser(UserRequest userRequest, MultipartFile profilePicture) {

        if (this.userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(userRequest.getUsername(), userRequest.getEmail())) {
            throw new BadRequestException("This user already exists, try signing in insead");
        }

        UserInfo userEntity = UserMapper.toUser(userRequest);
        if (profilePicture != null) {
            MediaDto mediaDto = this.mediaService.saveMedia(profilePicture);
            userEntity.setProfilePicture(MediaMapper.toMedia(mediaDto));
        }
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        this.userRepository.save(userEntity);
        return UserMapper.toUserResponse(userEntity);
    }

    @Override
    public List<UserResponse> getUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getUser(UUID userId) {
        UserInfo userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return UserMapper.toUserResponse(userEntity);
    }

    @Transactional
    @Override
    public UserResponse updateUser(UserRequest userRequest, UUID userId, MultipartFile profilePicture) {
        UserInfo userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        if (profilePicture != null) {
            MediaDto mediaDto = this.mediaService.saveMedia(profilePicture);
            userEntity.setProfilePicture(MediaMapper.toMedia(mediaDto));
        }

        userEntity.setFirstname(userRequest.getFirstname());
        userEntity.setLastname(userRequest.getLastname());
        this.userRepository.save(userEntity);

        return UserMapper.toUserResponse(userEntity);
    }

    @Transactional
    @Override
    public void deleteUser(UUID userId) {
        UserInfo userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        if (userEntity.getProfilePicture() != null) {
            this.mediaService.deleteMedia(userEntity.getProfilePicture().getId());
        }

        this.userRepository.deleteById(userId);
    }

}
