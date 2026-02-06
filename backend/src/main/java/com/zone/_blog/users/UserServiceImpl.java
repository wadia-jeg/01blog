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

import com.zone._blog.auth.Role;
import com.zone._blog.exceptions.BadRequestException;
import com.zone._blog.exceptions.ResourceNotFoundException;
import com.zone._blog.exceptions.UnauthorizedException;
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

    public UserServiceImpl(
            UserRepository userRepository,
            MediaService mediaService,
            PasswordEncoder passwordEncoder
    ) {
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

    @Override
    public UserInfo getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (UserInfo) authentication.getPrincipal();
    }

    @Transactional
    @Override
    public UserResponse addUser(UserRequest userRequest, MultipartFile profilePicture) {

        if (this.userRepository.existsByUsernameIgnoreCaseOrEmailIgnoreCase(userRequest.username(), userRequest.email())) {
            throw new BadRequestException("This user already exists, try signing in insead");
        }

        UserInfo userEntity = UserMapper.toUser(userRequest);
        if (profilePicture != null) {
            MediaDto mediaDto = this.mediaService.saveMedia(profilePicture);
            userEntity.setProfilePicture(MediaMapper.toMedia(mediaDto));
        }
        userEntity.setPassword(passwordEncoder.encode(userRequest.password()));
        this.userRepository.save(userEntity);
        return UserMapper.toUserResponse(userEntity);
    }

    @Override
    public List<UserResponse> getUsers() {
        UserInfo currentUser = this.getCurrentUser();

        return this.userRepository.findAllUsers(currentUser.getId());
    }

    @Override
    public UserResponse getUser(UUID userId) {
        UserInfo currentUser = this.getCurrentUser();

        UserResponse user = this.userRepository.findUserById(userId, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        return user;
    }

    @Transactional
    @Override
    public UserResponse updateUser(UserRequest userRequest, UUID userId, MultipartFile profilePicture) {
        UserInfo currentUser = this.getCurrentUser();
        UserInfo userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        if (currentUser.getId() != userEntity.getId()) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (profilePicture != null) {
            MediaDto mediaDto = this.mediaService.saveMedia(profilePicture);
            userEntity.setProfilePicture(MediaMapper.toMedia(mediaDto));
        }

        userEntity.setFirstname(userRequest.firstname());
        userEntity.setLastname(userRequest.lastname());
        this.userRepository.save(userEntity);

        return UserMapper.toUserResponse(userEntity);
    }

    @Transactional
    @Override
    public void deleteUser(UUID userId) {
        UserInfo currentUser = this.getCurrentUser();

        UserInfo userEntity = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User  not found"));

        if (currentUser.getId() != userEntity.getId()
                || currentUser.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Unauthorized access");
        }

        if (userEntity.getProfilePicture() != null) {
            this.mediaService.deleteMedia(userEntity.getProfilePicture().getId());
        }

        this.userRepository.deleteById(userId);
    }

}
