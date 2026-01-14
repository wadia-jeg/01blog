package com.zone._blog.users;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

public interface UserService extends UserDetailsService {

    public UserResponse addUser(UserRequest userRequest, MultipartFile profilePic);

    public UserResponse getUser(UUID userId);

    public UserResponse getCurrentUser();

    public List<UserResponse> getUsers();

    public UserResponse updateUser(UserRequest userRequest, UUID userId, MultipartFile profilePic);

    public void deleteUser(UUID userId);

}
