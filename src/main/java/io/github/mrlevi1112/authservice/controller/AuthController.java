package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.dto.AdminAccessDTO;
import io.github.mrlevi1112.authservice.dto.LogInDTO;
import io.github.mrlevi1112.authservice.dto.SignUpDTO;
import io.github.mrlevi1112.authservice.dto.TokenDTO;
import io.github.mrlevi1112.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(AuthServiceConstants.Security.AUTH_API_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(AuthServiceConstants.Security.SIGNUP_ENDPOINT)
    public ResponseEntity<TokenDTO> signup(@Valid @RequestBody SignUpDTO request) {
        TokenDTO response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(AuthServiceConstants.Security.LOGIN_ENDPOINT)
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LogInDTO request) {
        TokenDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin-access")
    public ResponseEntity<TokenDTO> claimAdminAccess(
            @Valid @RequestBody AdminAccessDTO request,
            Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Must be logged in");
        }
        TokenDTO response = authService.claimAdminAccess(request.getSecret(), authentication.getName());
        return ResponseEntity.ok(response);
    }
}
