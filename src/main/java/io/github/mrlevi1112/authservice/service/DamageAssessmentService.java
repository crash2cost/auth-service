package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.dto.DamageAssessmentDTO;
import io.github.mrlevi1112.authservice.dto.ReportSyncDTO;
import io.github.mrlevi1112.authservice.model.DamageAssessment;
import io.github.mrlevi1112.authservice.repository.DamageAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DamageAssessmentService {
    private final DamageAssessmentRepository assessmentRepository;
    private final RestTemplate restTemplate;

    @Value("${report.service.url:http://localhost:8003}")
    private String reportServiceUrl;

    public DamageAssessment saveAssessment(String authHeader, DamageAssessmentDTO dto, String userId) {
        DamageAssessment assessment = new DamageAssessment();
        assessment.setUserId(userId);
        assessment.setImageId(dto.getImageId());
        assessment.setTotalCost(dto.getTotalCost());
        assessment.setTotalLoss(dto.isTotalLoss());
        assessment.setDamageAreas(dto.getDamageAreas().stream()
                .map(da -> new DamageAssessment.DamageArea(da.getArea(), da.getSeverity(), da.getCost(), da.getDescription()))
                .toList());
        assessment.setAssessmentDate(LocalDateTime.now());
        DamageAssessment saved = assessmentRepository.save(assessment);

        try {
            String url = reportServiceUrl + "/api/reports/damage-assessment";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", authHeader);

            ReportSyncDTO report = new ReportSyncDTO();
            report.setImageId(saved.getImageId());
            report.setDamageAreas(saved.getDamageAreas().stream()
                .map(da -> new ReportSyncDTO.DamageAreaDTO(da.getArea(), da.getSeverity(), da.getCost(), da.getDescription()))
                .toList());
            report.setTotalCost(saved.getTotalCost());
            report.setTotalLoss(saved.isTotalLoss());

            HttpEntity<ReportSyncDTO> request = new HttpEntity<>(report, headers);
            restTemplate.postForObject(url, request, Object.class);
            saved.setSyncedToReportService(true);
            assessmentRepository.save(saved);
        } catch (RestClientException e) {
            log.error("Failed to sync assessment {} to report-service: {}", saved.getId(), e.getMessage(), e);
            saved.setSyncedToReportService(false);
            assessmentRepository.save(saved);
        }

        return saved;
    }

    public List<DamageAssessment> getUserAssessments(String userId) {
        return assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);
    }

    public Page<DamageAssessment> getAllAssessments(Pageable pageable) {
        return assessmentRepository.findAllBy(pageable);
    }

    public Optional<DamageAssessment> getAssessmentByImageId(String imageId) {
        return assessmentRepository.findByImageId(imageId);
    }

    public void deleteAssessment(String assessmentId, String userId) {
        DamageAssessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assessment not found"));
        if (!assessment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
        assessmentRepository.deleteById(assessmentId);
    }

    public void deleteAllUserAssessments(String userId) {
        List<DamageAssessment> assessments = assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);
        assessmentRepository.deleteAll(assessments);
    }
}
