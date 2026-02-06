package com.zone._blog.users;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.users.dto.UserRequest;
import com.zone._blog.users.dto.UserResponse;

@RestController
@RequestMapping("${app.api.v1}/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            // @RequestPart(name = "file", required = false) MultipartFile profilePic,
            // @Valid @RequestPart("info") UserRequest userRequest

            @Validated @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userService.addUser(userRequest, null));
    }

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(this.userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(this.userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            // @RequestPart(name = "file", required = false) MultipartFile profilePic,
            // @Valid @RequestPart("info") UserRequest userRequest,
            @Validated @RequestBody UserRequest userRequest,
            @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.userService.updateUser(userRequest, id, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        this.userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body("User deleted Succefully");
    }

}
