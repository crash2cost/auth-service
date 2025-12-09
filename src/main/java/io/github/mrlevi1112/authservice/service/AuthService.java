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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public TokenDTO signup(SignUpDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        return buildTokenResponse(user);
    }

    public TokenDTO login(LogInDTO request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException(AuthServiceConstants.Security.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(AuthServiceConstants.Security.INVALID_CREDENTIALS);
        }

        return buildTokenResponse(user);
    }

    private TokenDTO buildTokenResponse(User user) {
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return TokenDTO.builder()
                .tokenAccess(token)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .build();
    }
}
