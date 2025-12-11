package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.common.enums.UserRole;
import io.github.mrlevi1112.authservice.dto.LogInDTO;
import io.github.mrlevi1112.authservice.dto.SignUpDTO;
import io.github.mrlevi1112.authservice.dto.TokenDTO;
import io.github.mrlevi1112.authservice.model.User;
import io.github.mrlevi1112.authservice.repository.UserRepository;
import io.github.mrlevi1112.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final String Jwt = null;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public TokenDTO signup(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new RuntimeException(AuthServiceConstants.AuthMessages.USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException(AuthServiceConstants.AuthMessages.EMAIL_EXISTS);
        }

        User user = User.builder()
                .username(signUpDTO.getUsername())
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return TokenDTO.builder()
                .tokenAccess(token)
                .tokenType(AuthServiceConstants.Security.BEARER_PREFIX.trim())
                .role(user.getRole().name())
                .build();
    }

    public TokenDTO login(LogInDTO logInDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInDTO.getUsername(),
                        logInDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(logInDTO.getUsername())
                .orElseThrow(() -> new RuntimeException(AuthServiceConstants.AuthMessages.USERNAME_NOT_FOUND));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return TokenDTO.builder()
                .tokenAccess(token)
                .tokenType(AuthServiceConstants.Security.BEARER_PREFIX.trim())
                .role(user.getRole().name())
                .build();
    }
}
