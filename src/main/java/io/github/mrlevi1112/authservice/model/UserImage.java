package io.github.mrlevi1112.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_images")
public class UserImage {
    @Id
    private String id;
    
    private String userId;
    private String filename;
    private String contentType;
    private byte[] data;
    private Long size;
    private LocalDateTime uploadDate;
}
