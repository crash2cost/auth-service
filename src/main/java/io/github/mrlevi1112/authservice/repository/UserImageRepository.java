package io.github.mrlevi1112.authservice.repository;

import io.github.mrlevi1112.authservice.model.UserImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserImageRepository extends MongoRepository<UserImage, String> {
    List<UserImage> findByUserId(String userId);
    void deleteByUserId(String userId);
}
