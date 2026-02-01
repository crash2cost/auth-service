package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.dto.ErrorDTO;
import io.github.mrlevi1112.authservice.dto.LogInDTO;
import io.github.mrlevi1112.authservice.dto.SignUpDTO;
import io.github.mrlevi1112.authservice.dto.TokenDTO;
import io.github.mrlevi1112.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;

import java.time.LocalDateTime;

@RestController
@RequestMapping(AuthServiceConstants.Security.AUTH_API_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(AuthServiceConstants.Security.SIGNUP_ENDPOINT)
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpDTO request) {
        try {
            TokenDTO response = authService.signup(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorDTO error = ErrorDTO.builder()
                    .message(e.getMessage())
                    .status(400)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping(AuthServiceConstants.Security.LOGIN_ENDPOINT)
    public ResponseEntity<?> login(@Valid @RequestBody LogInDTO request) {
        try {
            TokenDTO response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ErrorDTO error = ErrorDTO.builder()
                    .message(e.getMessage())
                    .status(AuthServiceConstants.Security.HTTP_UNAUTHORIZED)
                    .timestamp(LocalDateTime.now())
                    .build();
            return ResponseEntity.status(AuthServiceConstants.Security.HTTP_UNAUTHORIZED)
                    .body(error);
        }
    }
}
