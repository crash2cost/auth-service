package io.github.mrlevi1112.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "damage_assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamageAssessment {
    @Id
    private String id;
    @Indexed
    private String userId;
    @Indexed
    private String imageId;
    private List<DamageArea> damageAreas;
    private double totalCost;
    private boolean totalLoss;
    private LocalDateTime assessmentDate;
    private boolean syncedToReportService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DamageArea {
        private String area;
        private int severity;
        private double cost;
        private String description;
    }
}
