package com.bondle.shortenurl.service;

import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import com.bondle.shortenurl.entity.Url;
import com.bondle.shortenurl.repository.ShortenUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class URLShortenServiceTest {
	private static final String LONG_SHORT_URL_CACHE_NAME = "longURL-shortURL-cache";
	private static final String SHORT_LONG_URL_CACHE_NAME = "shortURL-longURL-cache";

	@InjectMocks
	URLShortenServiceImpl URLShortenService;
	@Mock
	URLShortener urlShortener;
	@Mock
	ShortenUrlRepository shortenUrlRepository;
	@Mock
	CacheManager cacheManager;

	@Test
	void createShortUrl_get_from_card_should_success() {
		String longUrl = "http://long.url.com";
		String shortUrl = "http://short.url.com";
		ShortUrlRequest shortUrlRequest = new ShortUrlRequest();
		shortUrlRequest.setUrl(longUrl);
		Url shortUrlEntity = Url.builder().shortenedUrl(shortUrl).originalUrl(shortUrlRequest.getUrl()).createdAt(LocalDateTime.now()).build();
		Cache mockedCache = mock(Cache.class);

		given(cacheManager.getCache(LONG_SHORT_URL_CACHE_NAME)).willReturn(mockedCache);
		given(mockedCache.get(shortUrlRequest.getUrl(), Url.class)).willReturn(shortUrlEntity);

		ShortUrlResponse response = URLShortenService.createShortUrl(shortUrlRequest);

		verify(shortenUrlRepository, never()).save(any(Url.class));
		assertEquals(shortUrlEntity.getOriginalUrl(), response.getUrl());
		assertEquals(shortUrlEntity.getShortenedUrl(), response.getShortenedUrl());
	}

	@Test
	void createShortUrl_get_not_from_card_should_success() {
		String longUrl = "http://long.url.com";
		String shortUrl = "http://short.url.com";
		ShortUrlRequest shortUrlRequest = new ShortUrlRequest();
		shortUrlRequest.setUrl(longUrl);
		Url shortUrlEntity = Url.builder().shortenedUrl(shortUrl).originalUrl(shortUrlRequest.getUrl()).createdAt(LocalDateTime.now()).build();
		Cache mockedCache = mock(Cache.class);

		given(cacheManager.getCache(LONG_SHORT_URL_CACHE_NAME)).willReturn(mockedCache);
		given(mockedCache.get(shortUrlRequest.getUrl(), Url.class)).willReturn(null);
		given(urlShortener.shortenURL()).willReturn(shortUrl);

		ShortUrlResponse response = URLShortenService.createShortUrl(shortUrlRequest);

		verify(shortenUrlRepository).save(any(Url.class));
		verify(urlShortener).shortenURL();
		assertEquals(shortUrlEntity.getOriginalUrl(), response.getUrl());
		assertEquals(shortUrlEntity.getShortenedUrl(), response.getShortenedUrl());
	}

	@Test
	void getLongUrl_get_from_card_should_success() {
		String longUrl = "http://long.url.com";
		String shortUrlKey = "abcdef";
		Cache mockedCache = mock(Cache.class);
		ReflectionTestUtils.setField(URLShortenService, "shortenerDomain", "localhost:8080");

		given(cacheManager.getCache(SHORT_LONG_URL_CACHE_NAME)).willReturn(mockedCache);
		given(mockedCache.get(shortUrlKey, String.class)).willReturn(longUrl);

		String longUrlResult = URLShortenService.getLongUrl(shortUrlKey);

		verify(shortenUrlRepository, never()).findByShortenedUrl(anyString());
		assertEquals(longUrlResult, longUrl);
	}

	@Test
	void getLongUrl_get_not_from_card_should_success() {
		String longUrl = "http://long.url.com";
		String shortUrlKey = "abcdef";
		Cache mockedCache = mock(Cache.class);
		ReflectionTestUtils.setField(URLShortenService, "shortenerDomain", "localhost:8080");

		given(cacheManager.getCache(SHORT_LONG_URL_CACHE_NAME)).willReturn(mockedCache);
		given(mockedCache.get(shortUrlKey, String.class)).willReturn(null);
		given(shortenUrlRepository.findByShortenedUrl(anyString())).willReturn(Arrays.asList(Url.builder().originalUrl(longUrl).build()));

		String longUrlResult = URLShortenService.getLongUrl(shortUrlKey);

		verify(shortenUrlRepository).findByShortenedUrl(anyString());
		assertEquals(longUrlResult, longUrl);
	}
}
