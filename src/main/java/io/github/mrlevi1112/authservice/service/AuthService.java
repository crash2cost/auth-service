package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.builder.TokenDTOBuilder;
import io.github.mrlevi1112.authservice.builder.UserBuilder;
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
import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;

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
            throw new RuntimeException(AuthServiceConstants.Service.USERNAME_EXISTS);
        }
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException(AuthServiceConstants.Service.EMAIL_EXISTS);
        }

        User user = UserBuilder.buildNewUser(signUpDTO, passwordEncoder);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return TokenDTOBuilder.buildTokenResponse(token, user);
    }

    public TokenDTO login(LogInDTO logInDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logInDTO.getUsername(),
                        logInDTO.getPassword()
                )
        );

        User user = userRepository.findByUsername(logInDTO.getUsername())
                .orElseThrow(() -> new RuntimeException(AuthServiceConstants.Service.USERNAME_NOT_FOUND));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return TokenDTOBuilder.buildTokenResponse(token, user);
    }
}
