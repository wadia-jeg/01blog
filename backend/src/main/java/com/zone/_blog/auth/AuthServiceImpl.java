package com.zone._blog.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.zone._blog.auth.dto.AuthRequest;
import com.zone._blog.auth.dto.AuthResult;
import com.zone._blog.auth.dto.RefreshTokenResponse;
import com.zone._blog.users.UserInfo;
import com.zone._blog.users.UserService;
import com.zone._blog.users.dto.UserResponse;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            JwtService jwtService,
            CookieService cookieService,
            UserService userService,
            AuthenticationManager authenticationManager,
            RefreshTokenService refreshTokenService
    ) {

        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResult login(AuthRequest authRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password())
        );

        UserInfo user = (UserInfo) authentication.getPrincipal();

        RefreshTokenResponse refreshToken = this.refreshTokenService.generateRefreshToken(user);
        String accessToken = jwtService.generateToken(refreshToken.user());

        return new AuthResult(refreshToken, accessToken);

    }

    @Override
    public void logout(String refreshToken) {
        UserInfo user = this.userService.getCurrentUser();

        // If you want to logout from all devices delete by user id
        this.refreshTokenService.deleteRefreshToken(refreshToken, user);
        this.cookieService.revokeCookie("refresh-token");

    }

    @Override
    public AuthResult refreshToken(String refreshToken) {
        RefreshTokenResponse newRefreshToken = this.refreshTokenService.refreshToken(refreshToken);
        UserResponse user = newRefreshToken.user();

        String newAccessToken = jwtService.generateToken(user);

        return new AuthResult(newRefreshToken, newAccessToken);
    }

}

/*
package com.zone._blog.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zone._blog.auth.dto.AuthRequest;
import com.zone._blog.auth.dto.AuthResponse;
import com.zone._blog.auth.dto.RefreshTokenResponse;


@RestController
@RequestMapping("${app.api.v1}/auth")
public class AuthController {

    @Value("app.api.v1")
    private String baseUrl;

    @Value("${spring-security.refresh-token.expiration}")
    private long refreshExpiration;

    private final AuthService authService;
    private final CookieService cookieService;
    private final JwtService jwtService;

    public AuthController(
            AuthService authService,
            CookieService cookieService,
            JwtService jwtService) {
        this.authService = authService;
        this.cookieService = cookieService;
        this.jwtService = jwtService;

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        RefreshTokenResponse refreshToken = this.authService.login(authRequest);
        ResponseCookie cookie = this.cookieService.generateCookie(
                "refresh-token",
                refreshToken.token(),
                this.refreshExpiration,
                baseUrl + "/auth/"
        );

        String AccessToken = jwtService.generateToken(refreshToken.user());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(refreshToken.user().username(), AccessToken));

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("refresh-token") String refreshToken) {
        this.authService.logout(refreshToken);
        this.cookieService.revokeCookie("refresh-token");
        return ResponseEntity.status(HttpStatus.OK).body("Logged out succefully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@CookieValue("refresh-token") String refreshToken) {
        RefreshTokenResponse newRefreshToken = this.authService.refreshToken(refreshToken);

        ResponseCookie cookie = this.cookieService.generateCookie(
                "refresh-token",
                newRefreshToken.token(),
                this.refreshExpiration,
                baseUrl + "/auth/"
        );

        String newAccessToken = jwtService.generateToken(newRefreshToken.user());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponse(newRefreshToken.user().username(), newAccessToken));
    }

}


/*Absolutely! Let’s summarize **everything we covered today** about your Spring Boot app, JWT, and refresh token flow — step by step, including best practices and common pitfalls. I’ll structure it clearly so it’s easy to reference.

---

## **1️⃣ User Retrieval & Post Creation**

* You had this issue:

```text
Persistent instance of 'Post' references an unsaved transient instance of 'UserInfo'
```

* **Cause:** When you mapped your `UserResponse` → `UserInfo` manually, Hibernate thought it was a **new entity**, not managed (detached).

* **Solution:**

  1. Always fetch the user from the **database** using `userRepository` when assigning to a post.
  2. Don’t map DTO → entity blindly for relationships; use the persisted entity.

Example:

```java
UserInfo currentUser = userRepository.findById(userId)
    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
post.setUser(currentUser);
```

---

## **2️⃣ Security & JWT Filter**

* Initially, your `JwtFilter` threw exceptions and returned 500 errors instead of 401.

* **Cause:** You were **casting `authentication.getPrincipal()` to `UserInfo`**, but Spring Security sometimes stores `String` or a different type if authentication fails.

* **Best practices:**

  1. Implement `AuthenticationEntryPoint` to handle **unauthorized requests**.
  2. Don’t manually catch exceptions in the filter to return 401; delegate to `AuthEntryPointJwt`.
  3. Only allow anonymous requests via `.permitAll()` in `SecurityConfig`.
  4. Keep JWT validation **stateless**; refresh token is **not used for normal API calls**.

---

## **3️⃣ JWT vs Refresh Token**

| Token                  | Lifespan          | Use                              |
| ---------------------- | ----------------- | -------------------------------- |
| **JWT (access token)** | Short (minutes)   | Authenticate API requests        |
| **Refresh token**      | Long (days/weeks) | Request new JWT when JWT expires |

* **JWT is sent in `Authorization: Bearer ...` header** for API calls.

* **Refresh token is sent to `/auth/refresh`** to get a new JWT.

* **Rotation:** Each time you use a refresh token, **delete old → issue new**. This prevents reuse if stolen.

* **Important:** Refresh token does **not “refresh itself”**; it’s used to refresh JWT.

---

## **4️⃣ Handling Expiration vs Logout**

* **JWT expired:** Use refresh token to get a new JWT.
* **Refresh token expired:** Must log in again; user cannot refresh.
* **Logout:** Delete refresh token from DB. JWT will eventually expire; cannot refresh anymore.

**Rule:** Front-end must handle 401:

* If JWT expired → call `/refresh` with refresh token.
* If refresh token invalid/expired → redirect to login.

---

## **5️⃣ Refresh Token Implementation Notes**

* Fix `@Value` injection:

```java
@Value("${spring-security.jwt.refresh-expiration}")
private long refreshExpiration;
```

* Rotate tokens:

```java
refreshTokenRepository.delete(oldToken);
RefreshTokenResponse newRefresh = generateRefreshToken(user);
```

* Generate **JWT and new refresh token** atomically using `@Transactional`.

* Don’t save twice; your previous code saved refresh token twice.

* Always separate **JWT expiration** vs **refresh token expiration**. Example:

```properties
spring-security.jwt.expiration=900000         # 15 min
spring-security.jwt.refresh-expiration=604800000  # 7 days
```

* DTOs returned to front-end:

```json
{
  "accessToken": "<JWT>",
  "refreshToken": "<RefreshToken>"
}
```

---

## **6️⃣ Recommended Flow**

1. **Login**

   * Return JWT + refresh token.

2. **Access API**

   * Send JWT in Authorization header.

3. **JWT expires**

   * Send refresh token to `/auth/refresh`.
   * Server validates refresh token:

     * If valid → issue new JWT + new refresh token.
     * If expired → force login.

4. **Logout**

   * Delete refresh token from DB.

---

## **7️⃣ Best Practices & Key Takeaways**

* Always fetch **persistent entities** from DB when assigning relationships (e.g., `Post.user`).
* Use `AuthenticationEntryPoint` to return proper 401 instead of manual filter exceptions.
* JWT = short-lived access, stateless; Refresh token = long-lived, stored in DB, single-use.
* Rotate refresh tokens to prevent replay attacks.
* Keep **JWT & refresh token expiration separate**.
* Front-end must handle **401 vs refresh**:

  * JWT expired → refresh
  * Refresh token invalid/expired → login

---

If you want, I can also **draw a single flow diagram** showing **login → JWT → refresh → rotation → logout** specifically for your Spring Boot app. It makes all the relationships and decisions crystal clear.

Do you want me to make that diagram?
 */
