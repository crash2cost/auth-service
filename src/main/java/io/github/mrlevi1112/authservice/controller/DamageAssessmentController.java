package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.model.DamageAssessment;
import io.github.mrlevi1112.authservice.security.JwtUtil;
import io.github.mrlevi1112.authservice.service.DamageAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class DamageAssessmentController {
    private final DamageAssessmentService assessmentService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<DamageAssessment> saveAssessment(
            @RequestHeader("Authorization") String token,
            @RequestBody DamageAssessment assessment) {
        String userId = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        assessment.setUserId(userId);
        return ResponseEntity.ok(assessmentService.saveAssessment(assessment));
    }

    @GetMapping
    public ResponseEntity<List<DamageAssessment>> getUserAssessments(
            @RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return ResponseEntity.ok(assessmentService.getUserAssessments(userId));
    }

    @GetMapping("/image/{imageId}")
    public ResponseEntity<DamageAssessment> getAssessmentByImageId(
            @PathVariable String imageId) {
        return assessmentService.getAssessmentByImageId(imageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{assessmentId}")
    public ResponseEntity<Void> deleteAssessment(
            @RequestHeader("Authorization") String token,
            @PathVariable String assessmentId) {
        try {
            String userId = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            assessmentService.deleteAssessment(assessmentId, userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllAssessments(
            @RequestHeader("Authorization") String token) {
        String userId = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        assessmentService.deleteAllUserAssessments(userId);
        return ResponseEntity.noContent().build();
    }
}
