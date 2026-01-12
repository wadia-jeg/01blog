package com.zone._blog.users;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zone._blog.auth.Role;
import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserInfo user = this.userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(login, login).orElseThrow(
                () -> new UsernameNotFoundException("User " + login + " not found"));

        // List<GrantedAuthority> authorities;
        // if (user.getRole() == Role.ADMIN) {
        //     authorities = List.of(
        //             new SimpleGrantedAuthority("ROLE_ADMIN"),
        //             new SimpleGrantedAuthority("ROLE_REGULAR")
        //     );
        // } else {
        //     authorities = List.of(
        //             new SimpleGrantedAuthority("ROLE_REGULAR")
        //     );
        // }
        return new UserInfo(
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getRole()
        );
    }

    public UserResponse addUser(UserRequest userRequest) {
        UserInfo userEntity = UserMapper.toUser(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        this.userRepository.save(userEntity);
        return UserMapper.toUserResponse(userEntity);
    }

}
