package io.github.mrlevi1112.authservice.builder;

import io.github.mrlevi1112.authservice.common.enums.UserRole;
import io.github.mrlevi1112.authservice.dto.SignUpDTO;
import io.github.mrlevi1112.authservice.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserBuilder {
    
    private String id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;

    private UserBuilder() {}

    public UserBuilder id(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder password(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder role(UserRole role) {
        this.role = role;
        return this;
    }

    public UserBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User build() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .createdAt(createdAt)
                .build();
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static User buildNewUser(SignUpDTO signUpDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(signUpDTO.getUsername())
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
