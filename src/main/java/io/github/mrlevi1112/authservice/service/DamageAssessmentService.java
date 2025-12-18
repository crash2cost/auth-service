package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.model.DamageAssessment;
import io.github.mrlevi1112.authservice.repository.DamageAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DamageAssessmentService {
    private final DamageAssessmentRepository assessmentRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${report.service.url:http://localhost:8003}")
    private String reportServiceUrl;

    public DamageAssessment saveAssessment(DamageAssessment assessment) {
        assessment.setAssessmentDate(LocalDateTime.now());
        DamageAssessment saved = assessmentRepository.save(assessment);
        
        // Also save to report-service
        try {
            String url = reportServiceUrl + "/api/reports/damage-assessment";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            
            // Create report object
            ReportDTO report = new ReportDTO();
            report.setImageId(saved.getImageId());
            report.setDamageAreas(saved.getDamageAreas().stream()
                .map(da -> new ReportDTO.DamageAreaDTO(da.getArea(), da.getSeverity(), da.getCost(), da.getDescription()))
                .toList());
            report.setTotalCost(saved.getTotalCost());
            report.setTotalLoss(saved.isTotalLoss());
            
            HttpEntity<ReportDTO> request = new HttpEntity<>(report, headers);
            restTemplate.postForObject(url, request, Object.class);
        } catch (Exception e) {
            System.err.println("Failed to save to report-service: " + e.getMessage());
        }
        
        return saved;
    }

    public List<DamageAssessment> getUserAssessments(String userId) {
        return assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);
    }

    public Optional<DamageAssessment> getAssessmentByImageId(String imageId) {
        return assessmentRepository.findByImageId(imageId);
    }

    public void deleteAssessment(String assessmentId, String userId) {
        Optional<DamageAssessment> assessment = assessmentRepository.findById(assessmentId);
        if (assessment.isEmpty() || !assessment.get().getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this assessment");
        }
        assessmentRepository.deleteById(assessmentId);
    }

    public void deleteAllUserAssessments(String userId) {
        List<DamageAssessment> assessments = assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);
        assessmentRepository.deleteAll(assessments);
    }
    
    // DTO for report service
    private static class ReportDTO {
        private String imageId;
        private List<DamageAreaDTO> damageAreas;
        private Double totalCost;
        private Boolean totalLoss;
        
        public String getImageId() { return imageId; }
        public void setImageId(String imageId) { this.imageId = imageId; }
        public List<DamageAreaDTO> getDamageAreas() { return damageAreas; }
        public void setDamageAreas(List<DamageAreaDTO> damageAreas) { this.damageAreas = damageAreas; }
        public Double getTotalCost() { return totalCost; }
        public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
        public Boolean getTotalLoss() { return totalLoss; }
        public void setTotalLoss(Boolean totalLoss) { this.totalLoss = totalLoss; }
        
        private static class DamageAreaDTO {
            private String area;
            private Integer severity;
            private Double cost;
            private String description;
            
            public DamageAreaDTO(String area, Integer severity, Double cost, String description) {
                this.area = area;
                this.severity = severity;
                this.cost = cost;
                this.description = description;
            }
            
            public String getArea() { return area; }
            public Integer getSeverity() { return severity; }
            public Double getCost() { return cost; }
            public String getDescription() { return description; }
        }
    }
}
