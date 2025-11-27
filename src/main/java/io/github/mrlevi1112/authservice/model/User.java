package io.github.mrlevi1112.authservice.model;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.common.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.management.relation.Role;
import java.time.LocalDateTime;

import static io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants.Validation.MAX_PASSWORD_LENGTH;
import static io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants.Validation.MIN_PASSWORD_LENGTH;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = AuthServiceConstants.Database.USERS_COLLECTION)
public class User {

    @Id
    private String id;

    @NotNull(message = AuthServiceConstants.Validation.NULL_USERNAME)
    @NotBlank(message = AuthServiceConstants.Validation.BLANK_USERNAME)
    @Indexed(unique = true)
    private String username;

    @Email(message = AuthServiceConstants.Validation.INVALID_EMAIL)
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = AuthServiceConstants.Validation.BLANK_PASSWORD)
    @NotNull(message = AuthServiceConstants.Validation.NULL_PASSWORD)
    @Size(min = MIN_PASSWORD_LENGTH , max = MAX_PASSWORD_LENGTH , message = AuthServiceConstants.Validation.PASSWORD_LENGTH_MESSAGE)
    @Pattern(
            regexp = AuthServiceConstants.Validation.PASSWORD_REGEXP,
            message = AuthServiceConstants.Validation.PASSWORD_REQUIREMENTS)
    private String password;

    @NotNull(message = AuthServiceConstants.Validation.NULL_USER_ROLE)
    private UserRole role;

    @CreatedDate
    private LocalDateTime createdAt;
}