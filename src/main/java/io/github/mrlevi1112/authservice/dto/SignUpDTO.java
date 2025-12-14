package io.github.mrlevi1112.authservice.dto;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants.Validation.MAX_PASSWORD_LENGTH;
import static io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants.Validation.MIN_PASSWORD_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

    @NotBlank(message = AuthServiceConstants.Validation.BLANK_USERNAME)
    @Size(min = AuthServiceConstants.Validation.MIN_USERNAME_LENGTH, max = AuthServiceConstants.Validation.MAX_USERNAME_LENGTH, message = AuthServiceConstants.Validation.USERNAME_LENGTH)
    private String username;

    @NotBlank(message = AuthServiceConstants.Validation.BLANK_EMAIL)
    @Email(message = AuthServiceConstants.Validation.INVALID_EMAIL)
    private String email;

    @NotBlank(message = AuthServiceConstants.Validation.BLANK_PASSWORD)
    @NotNull(message = AuthServiceConstants.Validation.NULL_PASSWORD)
    @Size(min = MIN_PASSWORD_LENGTH , max = MAX_PASSWORD_LENGTH , message = AuthServiceConstants.Validation.PASSWORD_LENGTH_MESSAGE)
    @Pattern(
            regexp = AuthServiceConstants.Validation.PASSWORD_REGEXP,
            message = AuthServiceConstants.Validation.PASSWORD_REQUIREMENTS)
    private String password;
}