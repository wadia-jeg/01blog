package com.zone._blog.auth;

import com.zone._blog.auth.dto.AuthRequest;
import com.zone._blog.auth.dto.AuthResult;

public interface AuthService {

    public AuthResult login(AuthRequest authRequest);

    public void logout(String refreshToken);

    public AuthResult refreshToken(String refreshToken);
}
