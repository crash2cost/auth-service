package io.github.mrlevi1112.authservice.service;

import io.github.mrlevi1112.authservice.common.constants.AuthServiceConstants;
import io.github.mrlevi1112.authservice.dto.ImageDTO;
import io.github.mrlevi1112.authservice.model.UserImage;
import io.github.mrlevi1112.authservice.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final UserImageRepository userImageRepository;

    public ImageDTO uploadImage(String userId, MultipartFile file) throws IOException {
        UserImage image = UserImage.builder()
                .userId(userId)
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .data(file.getBytes())
                .size(file.getSize())
                .uploadDate(LocalDateTime.now())
                .build();

        UserImage savedImage = userImageRepository.save(image);

        return convertToDTO(savedImage);
    }

    public List<ImageDTO> getUserImages(String userId) {
        return userImageRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserImage getImage(String imageId) {
        return userImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException(AuthServiceConstants.Images.IMAGE_NOT_FOUND));
    }

    public void deleteImage(String imageId, String userId) {
        UserImage image = getImage(imageId);
        if (!image.getUserId().equals(userId)) {
            throw new RuntimeException(AuthServiceConstants.Images.UNOTHORIZED_TO_DELETE_IMAGE);
        }
        userImageRepository.deleteById(imageId);
    }

    private ImageDTO convertToDTO(UserImage image) {
        return ImageDTO.builder()
                .id(image.getId())
                .filename(image.getFilename())
                .contentType(image.getContentType())
                .size(image.getSize())
                .uploadDate(image.getUploadDate().toString())
                .build();
    }
}
