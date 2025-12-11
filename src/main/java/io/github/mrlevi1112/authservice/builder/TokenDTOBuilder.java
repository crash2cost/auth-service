package io.github.mrlevi1112.authservice.builder;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.dto.TokenDTO;
import io.github.mrlevi1112.authservice.model.User;

public class TokenDTOBuilder {
    
    private String tokenAccess;
    private String tokenType;
    private String role;

    private TokenDTOBuilder() {}

    public TokenDTOBuilder tokenAccess(String tokenAccess) {
        this.tokenAccess = tokenAccess;
        return this;
    }

    public TokenDTOBuilder tokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public TokenDTOBuilder role(String role) {
        this.role = role;
        return this;
    }

    public TokenDTO build() {
        return TokenDTO.builder()
                .tokenAccess(tokenAccess)
                .tokenType(tokenType)
                .role(role)
                .build();
    }

    public static TokenDTOBuilder builder() {
        return new TokenDTOBuilder();
    }

    public static TokenDTO buildTokenResponse(String token, User user) {
        return TokenDTO.builder()
                .tokenAccess(token)
                .tokenType(AuthServiceConstants.Security.BEARER_PREFIX.trim())
                .role(user.getRole().name())
                .build();
    }
}
