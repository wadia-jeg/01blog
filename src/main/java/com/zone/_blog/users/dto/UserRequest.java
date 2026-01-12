package com.zone._blog.users.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.zone._blog.auth.Role;

public record UserRequest(
        String username,
        String email,
        String password,
        Role role,
        String firstname,
        String lastname
        ) {

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == Role.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_REGULAR")
            );
        }
        return List.of(
                new SimpleGrantedAuthority("ROLE_REGULAR")
        );
    }

}
