package io.github.mrlevi1112.authservice.dto;

import io.github.mrlevi1112.authservice.common.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserDTO {
    private String id;
    private String username;
    private String email;
    private UserRole role;
    private LocalDateTime createdAt;
    private long assessmentCount;
}
