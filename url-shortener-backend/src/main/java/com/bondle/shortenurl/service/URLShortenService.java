package com.bondle.shortenurl.service;

import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface URLShortenService {
    ShortUrlResponse createShortUrl(ShortUrlRequest shortenRequest);

    String getLongUrl(String shortenKey);

    Page<ShortUrlResponse> listUrls(String shortenedUrl, Pageable pageable);
}
