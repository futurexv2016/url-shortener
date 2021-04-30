package com.bondle.shortenurl.service;

import com.bondle.shortenurl.entity.Url;
import com.bondle.shortenurl.dto.ShortUrlRequest;
import com.bondle.shortenurl.dto.ShortUrlResponse;
import com.bondle.shortenurl.repository.ShortenUrlRepository;
import com.bondle.shortenurl.util.URLMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service containing business logic to process shortening URL
 * @author david.ho
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class URLShortenServiceImpl implements URLShortenService {

	private static final String LONG_SHORT_URL_CACHE_NAME = "longURL-shortURL-cache";
	private static final String SHORT_LONG_URL_CACHE_NAME = "shortURL-longURL-cache";

	@Value("${shortener.shortenerDomain}")
	private String shortenerDomain;

	private final URLShortener urlShortener;
	private final CacheManager cacheManager;
	private final ShortenUrlRepository shortenUrlRepository;

	public ShortUrlResponse createShortUrl(ShortUrlRequest shortURLRequest) {
		String longUrl = shortURLRequest.getUrl();
		Url shortUrlEntity = getShortURLFromCache(longUrl);
		if (shortUrlEntity == null) {
			log.debug("Short URL is not in cache, create it..");
			shortUrlEntity = createShortURL(shortURLRequest, longUrl);
		} else {
			log.debug("Short URL is taken from cache");
		}
		log.info("Finish shortening long URL({}) to short URL({})", longUrl, shortUrlEntity.getShortenedUrl());
		return URLMapper.convertURLEntityToResponse(shortUrlEntity);
	}

	public String getLongUrl(String shortURLKey) {
		String shortUrl = shortURLKey.contains(shortenerDomain) ? shortURLKey : shortenerDomain + "/" + shortURLKey;
		String longUrl = getLongURLFromCache(shortURLKey);
		if (longUrl == null) {
			log.debug("Long URL is not in cache - get it from database..");
			longUrl = getLongURLFromDB(shortURLKey, shortUrl);
		} else {
			log.debug("Long URL({}) is obtained from cache", longUrl);
		}
		log.info("Finish getting long URL({}) for short URL({})", longUrl, shortUrl);
		return longUrl;
	}

	private Url createShortURL(ShortUrlRequest shortURLRequest, String longUrl) {
		Url shortUrlEntity = createShortURLEntity(shortURLRequest);
		shortenUrlRepository.save(shortUrlEntity);
		getLongShortURLCache().put(longUrl, shortUrlEntity);
		return shortUrlEntity;
	}

	private Url createShortURLEntity(ShortUrlRequest shortURLRequest) {
		Url shortUrlEntity = new Url();
		shortUrlEntity.setShortenedUrl(urlShortener.shortenURL());
		shortUrlEntity.setOriginalUrl(shortURLRequest.getUrl());
		shortUrlEntity.setCreatedAt(LocalDateTime.now());
		return shortUrlEntity;
	}

	private String getLongURLFromDB(String shortenKey, String shortenUrl) {
		List<Url> urlList = shortenUrlRepository.findByShortenedUrl(shortenUrl);
		if (CollectionUtils.isEmpty(urlList)) {
			throw new RuntimeException("This short URL does not exist!");
		}
		String longUrl = urlList.get(0).getOriginalUrl();
		getShortLongURLCache().put(shortenKey, longUrl);
		return longUrl;
	}

	public Page<ShortUrlResponse> listUrls(String shortUrlKey, Pageable pageable) {
		if (StringUtils.hasLength(shortUrlKey)) {
			return listUrlBySearching(shortUrlKey, pageable);
		}
		return shortenUrlRepository.findAll(pageable)
				.map(URLMapper::convertURLEntityToResponse);
	}

	private Page<ShortUrlResponse> listUrlBySearching(String shortUrlKey, Pageable pageable) {
		String shortenedUrlLike = new StringBuilder()
				.append(shortenerDomain).append("%").append(shortUrlKey.trim()).append("%").toString();
		Specification<Url> isShortUrl = ((root, criteriaQuery, cb) ->
				cb.like(root.get("shortenedUrl"), shortenedUrlLike));
		return shortenUrlRepository.findAll(isShortUrl, pageable)
				.map(URLMapper::convertURLEntityToResponse);
	}

	private Url getShortURLFromCache(String longUrl) {
		return getLongShortURLCache().get(longUrl, Url.class);
	}
	private String getLongURLFromCache(String shortUrlKey) {
		return getShortLongURLCache().get(shortUrlKey, String.class);
	}

	private Cache getLongShortURLCache() {
		return cacheManager.getCache(LONG_SHORT_URL_CACHE_NAME);
	}

	private Cache getShortLongURLCache() {
		return cacheManager.getCache(SHORT_LONG_URL_CACHE_NAME);
	}
}
