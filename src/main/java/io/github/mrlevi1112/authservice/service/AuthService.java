package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.common.enums.UserRole;
import io.github.mrlevi1112.authservice.dto.LogInDTO;
import io.github.mrlevi1112.authservice.dto.SignUpDTO;
import io.github.mrlevi1112.authservice.dto.TokenDTO;
import io.github.mrlevi1112.authservice.exception.UserAlreadyExistsException;
import io.github.mrlevi1112.authservice.exception.UserNotFoundException;
import io.github.mrlevi1112.authservice.model.User;
import io.github.mrlevi1112.authservice.repository.UserRepository;
import io.github.mrlevi1112.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Value("${admin.secret}")
    private String adminSecret;

    public TokenDTO signup(SignUpDTO signUpDTO) {
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new UserAlreadyExistsException(AuthServiceConstants.AuthMessages.USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new UserAlreadyExistsException(AuthServiceConstants.AuthMessages.EMAIL_EXISTS);
        }

        User user = createUser(signUpDTO);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return createTokenResponse(token, user.getRole().name());
    }

    public TokenDTO login(LogInDTO logInDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInDTO.getUsername(),
                        logInDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(logInDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException(AuthServiceConstants.AuthMessages.USERNAME_NOT_FOUND));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return createTokenResponse(token, user.getRole().name());
    }

    private User createUser(SignUpDTO signUpDTO) {
        return User.builder()
                .username(signUpDTO.getUsername())
                .email(signUpDTO.getEmail())
                .password(passwordEncoder.encode(signUpDTO.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public TokenDTO claimAdminAccess(String secret, String username) {
        if (!adminSecret.equals(secret)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid admin secret");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(AuthServiceConstants.AuthMessages.USERNAME_NOT_FOUND));

        if (user.getRole() == UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already an admin");
        }

        user.setRole(UserRole.ADMIN);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return createTokenResponse(token, user.getRole().name());
    }

    private TokenDTO createTokenResponse(String token, String role) {
        return TokenDTO.builder()
                .tokenAccess(token)
                .tokenType(AuthServiceConstants.Security.BEARER_PREFIX.trim())
                .role(role)
                .build();
    }
}
