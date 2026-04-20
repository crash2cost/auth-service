package io.github.mrlevi1112.authservice.dto;

import io.github.mrlevi1112.authservice.common.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeRoleDTO {
    @NotNull(message = "Role cannot be null")
    private UserRole role;
}
