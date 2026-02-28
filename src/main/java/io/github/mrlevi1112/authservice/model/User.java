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

import java.time.LocalDateTime;

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

    private String password;

    @NotNull(message = AuthServiceConstants.Validation.NULL_USER_ROLE)
    private UserRole role;

    @CreatedDate
    private LocalDateTime createdAt;
}
