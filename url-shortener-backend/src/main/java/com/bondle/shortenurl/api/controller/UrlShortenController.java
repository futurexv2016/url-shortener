package com.bondle.shortenurl.api.controller;

import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import com.bondle.shortenurl.service.URLShortenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Controller to expose Rest API of shorting URL for client
 *
 * @author david.ho
 */
@RequiredArgsConstructor
@RestController
@EnableSwagger2
@CrossOrigin(origins = "*", maxAge = 3600000)
@Slf4j
public class UrlShortenController implements UrlShortenApi {

	private final URLShortenService urlShortenService;

	@Override
	public ResponseEntity<Page<ShortUrlResponse>> listURL(String shortenedUrl, Pageable pageable) {
		logListURLs(shortenedUrl);
		return ResponseEntity.ok(urlShortenService.listUrls(shortenedUrl, pageable));
	}

	@Override
	public ResponseEntity<String> getLongURL(String shortenedUrl) {
		log.info("Start getting long URL for short URL: {}..", shortenedUrl);
		return ResponseEntity.ok(urlShortenService.getLongUrl(shortenedUrl));
	}

	@Override
	public ResponseEntity<ShortUrlResponse> shortenUrl(ShortUrlRequest shortUrlRequest) {
		log.info("Start shortening URL for short request: {}..", shortUrlRequest.toString());
		return new ResponseEntity<>(urlShortenService.createShortUrl(shortUrlRequest), HttpStatus.CREATED);
	}

	private void logListURLs(String shortUrlKey) {
		if (StringUtils.hasLength(shortUrlKey)) {
			log.info("Start searching URLs for shortURL key ({})..", shortUrlKey);
		} else {
			log.info("Start listing history URLs..");
		}
	}
}

