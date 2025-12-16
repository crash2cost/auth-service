package io.github.mrlevi1112.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private String id;
    private String filename;
    private String contentType;
    private Long size;
    private String uploadDate;
}
