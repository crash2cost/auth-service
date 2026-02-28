package io.github.mrlevi1112.authservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DamageAssessmentDTO {

    @NotBlank(message = "Image ID is required")
    private String imageId;

    @NotNull(message = "Damage areas are required")
    @Size(min = 1, max = 50, message = "Damage areas must contain between 1 and 50 entries")
    @Valid
    private List<DamageAreaDTO> damageAreas;

    @PositiveOrZero(message = "Total cost must be zero or positive")
    private double totalCost;

    private boolean totalLoss;

    @Data
    public static class DamageAreaDTO {
        private String area;
        private int severity;
        @PositiveOrZero(message = "Cost must be zero or positive")
        private double cost;
        private String description;
    }
}
