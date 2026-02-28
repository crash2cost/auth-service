package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.dto.DamageAssessmentDTO;
import io.github.mrlevi1112.authservice.model.DamageAssessment;
import io.github.mrlevi1112.authservice.service.DamageAssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class DamageAssessmentController {
    private final DamageAssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<DamageAssessment> saveAssessment(
            @RequestHeader("Authorization") String token,
            Authentication authentication,
            @Valid @RequestBody DamageAssessmentDTO dto) {
        return ResponseEntity.ok(assessmentService.saveAssessment(token, dto, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<DamageAssessment>> getUserAssessments(
            Authentication authentication) {
        return ResponseEntity.ok(assessmentService.getUserAssessments(authentication.getName()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DamageAssessment>> getAllAssessments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return ResponseEntity.ok(assessmentService.getAllAssessments(pageable));
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<DamageAssessment> getAssessmentByImageId(
            @PathVariable String imageId,
            Authentication authentication) {
        return assessmentService.getAssessmentByImageId(imageId)
                .filter(a -> a.getUserId().equals(authentication.getName()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{assessmentId}")
    public ResponseEntity<Void> deleteAssessment(
            Authentication authentication,
            @PathVariable String assessmentId) {
        assessmentService.deleteAssessment(assessmentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllAssessments(
            Authentication authentication) {
        assessmentService.deleteAllUserAssessments(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
