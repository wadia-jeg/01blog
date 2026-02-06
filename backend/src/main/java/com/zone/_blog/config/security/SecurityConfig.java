package com.zone._blog.config.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zone._blog.auth.AuthEntryPoint;
import com.zone._blog.auth.JwtFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.api.v1}")
    private String baseUrl;

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            AuthEntryPoint authEntryPoint,
            PasswordEncoder passwordEncoder
    ) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authEntryPoint = authEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            JwtFilter jwtFilter,
            HttpSecurity http
    ) throws Exception {
        http
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
                .csrf(csrf -> csrf
                .ignoringRequestMatchers(baseUrl + "/auth/**", baseUrl + "/users/register")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .exceptionHandling(
                        ex -> ex.authenticationEntryPoint(this.authEntryPoint)
                )
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/",
                                        baseUrl + "/auth/login",
                                        baseUrl + "/test-backend",
                                        baseUrl + "/users/register"
                                ).permitAll()
                                .requestMatchers(baseUrl + "/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).authenticationProvider(
                        this.authenticationProvider()
                ).addFilterBefore(
                        jwtFilter, UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200/");
        corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-XSRF-TOKEN", "Accept"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(this.userDetailsService);
        provider.setPasswordEncoder(this.passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}

/*

Double Submit Cookie pattern

The OPTIONS method is the heart of CORS. It’s called a Preflight Request.

When your frontend tries to send a request that is "non-simple" (like a POST with a JSON body or any request with an Authorization or X-XSRF-TOKEN header), the browser doesn't send your actual request immediately.

The Preflight Process:

    The Browser asks: It sends an OPTIONS request to your backend saying: "Hey, I'm about to send a POST with an X-XSRF-TOKEN header. Is that cool?"

    The Server responds: If your CORS config allows POST and X-XSRF-TOKEN, the server sends back a 200 OK with the correct CORS headers.

    The Browser acts: Only after receiving that "Yes," the browser sends your actual refresh-token or logout request.

If you remove OPTIONS from your allowedMethods, the browser's "ask" will be rejected with a 403 Forbidden, and your actual request will never even leave the browser.
 */
