package io.github.mrlevi1112.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogInDTO {

    @NotBlank(message = AuthServiceConstants.Security.INVALID_CREDENTIALS)
    private String username;

    @NotBlank(message = AuthServiceConstants.Security.INVALID_CREDENTIALS)
    private String password;
}
