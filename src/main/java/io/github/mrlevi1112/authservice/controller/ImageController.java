package io.github.mrlevi1112.authservice.controller;

import io.github.mrlevi1112.authservice.dto.ImageDTO;
import io.github.mrlevi1112.authservice.model.UserImage;
import io.github.mrlevi1112.authservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            ImageDTO uploadedImage = imageService.uploadImage(username, file);
            return ResponseEntity.ok(uploadedImage);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/my-images")
    public ResponseEntity<List<ImageDTO>> getMyImages(Authentication authentication) {
        String username = authentication.getName();
        List<ImageDTO> images = imageService.getUserImages(username);
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imageId) {
        try {
            UserImage image = imageService.getImage(imageId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getContentType()));
            headers.setContentLength(image.getSize());
            headers.setContentDispositionFormData("inline", image.getFilename());

            return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable String imageId,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            imageService.deleteImage(imageId, username);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
