package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
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

/**
 * REST controller for authentication endpoints.
 * Handles user signup and login operations.
 *
 * Exceptions are handled by GlobalExceptionHandler.
 */
@RestController
@RequestMapping(AuthServiceConstants.Security.AUTH_API_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user.
     *
     * @param request User registration data
     * @return TokenDTO containing JWT access token
     */
    @PostMapping(AuthServiceConstants.Security.SIGNUP_ENDPOINT)
    public ResponseEntity<TokenDTO> signup(@Valid @RequestBody SignUpDTO request) {
        TokenDTO response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticate a user.
     *
     * @param request User login credentials
     * @return TokenDTO containing JWT access token
     */
    @PostMapping(AuthServiceConstants.Security.LOGIN_ENDPOINT)
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LogInDTO request) {
        TokenDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
