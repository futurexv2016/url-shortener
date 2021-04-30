package com.bondle.shortenurl.dto;

import lombok.*;

import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrlResponse {
    private UUID id;
    private String url;
    private String shortenedUrl;
    private LocalDateTime createdAt;
}
