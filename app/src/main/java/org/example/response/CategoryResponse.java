package org.example.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponse {
    private Long Id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
