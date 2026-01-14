package com.zone._blog.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.auth.dto.AuthRequest;
import com.zone._blog.auth.dto.AuthResponse;
import com.zone._blog.users.UserInfo;

@RestController
@RequestMapping("${app.api.v1}/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(AuthRequest authRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword())
        );

        UserInfo user = (UserInfo) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new AuthResponse(user.getUsername(), token));

    }

}
