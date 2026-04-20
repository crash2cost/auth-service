package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.common.enums.UserRole;
import io.github.mrlevi1112.authservice.dto.AdminStatsDTO;
import io.github.mrlevi1112.authservice.dto.AdminUserDTO;
import io.github.mrlevi1112.authservice.model.DamageAssessment;
import io.github.mrlevi1112.authservice.model.User;
import io.github.mrlevi1112.authservice.repository.DamageAssessmentRepository;
import io.github.mrlevi1112.authservice.repository.UserImageRepository;
import io.github.mrlevi1112.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final DamageAssessmentRepository damageAssessmentRepository;
    private final UserImageRepository userImageRepository;
    private final MongoTemplate mongoTemplate;

    public Page<AdminUserDTO> getAllUsers(String search, Pageable pageable) {
        Page<User> users;
        if (search == null || search.isBlank()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByUsernameContainingIgnoreCase(search, pageable);
        }
        return users.map(this::toDTO);
    }

    public AdminUserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toDTO(user);
    }

    public AdminUserDTO changeUserRole(String id, UserRole newRole, String requestingUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getUsername().equals(requestingUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot change your own role");
        }

        user.setRole(newRole);
        userRepository.save(user);
        return toDTO(user);
    }

    public void deleteUser(String id, String requestingUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getUsername().equals(requestingUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot delete your own account");
        }

        damageAssessmentRepository.deleteByUserId(user.getUsername());
        userImageRepository.deleteByUserId(user.getUsername());
        userRepository.delete(user);
    }

    public AdminStatsDTO getStats() {
        long totalUsers = userRepository.count();
        long totalAdmins = userRepository.countByRole(UserRole.ADMIN);
        long totalAssessments = damageAssessmentRepository.count();

        double totalCostSum = 0;
        try {
            Aggregation aggregation = Aggregation.newAggregation(
                    Aggregation.group().sum("totalCost").as("total")
            );
            AggregationResults<Map> results = mongoTemplate.aggregate(
                    aggregation, "damage_assessments", Map.class
            );
            Map result = results.getUniqueMappedResult();
            if (result != null && result.get("total") != null) {
                totalCostSum = ((Number) result.get("total")).doubleValue();
            }
        } catch (Exception ignored) {
        }

        return AdminStatsDTO.builder()
                .totalUsers(totalUsers)
                .totalAdmins(totalAdmins)
                .totalAssessments(totalAssessments)
                .totalCostSum(totalCostSum)
                .build();
    }

    private AdminUserDTO toDTO(User user) {
        return AdminUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .assessmentCount(damageAssessmentRepository.countByUserId(user.getUsername()))
                .build();
    }
}
