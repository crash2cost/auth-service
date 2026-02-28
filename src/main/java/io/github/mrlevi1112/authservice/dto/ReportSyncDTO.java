package io.github.mrlevi1112.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportSyncDTO {
    private String imageId;
    private List<DamageAreaDTO> damageAreas;
    private Double totalCost;
    private Boolean totalLoss;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DamageAreaDTO {
        private String area;
        private Integer severity;
        private Double cost;
        private String description;
    }
}
