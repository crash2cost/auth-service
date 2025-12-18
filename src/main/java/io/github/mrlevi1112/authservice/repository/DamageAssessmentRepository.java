package io.github.mrlevi1112.authservice.repository;

import io.github.mrlevi1112.authservice.model.DamageAssessment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DamageAssessmentRepository extends MongoRepository<DamageAssessment, String> {
    List<DamageAssessment> findByUserIdOrderByAssessmentDateDesc(String userId);
    Optional<DamageAssessment> findByImageId(String imageId);
}
