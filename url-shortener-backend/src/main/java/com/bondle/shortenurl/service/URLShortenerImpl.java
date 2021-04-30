package com.bondle.shortenurl.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;

/**
 * To process logic of shortening URL.
 * Short-url algorithm is very simple for demo purpose, there might be identical short url if the number of short url is big enough.
 * @author david.ho
 */

@Component
@Slf4j
public class URLShortenerImpl implements URLShortener {
	@Value("${shortener.keyLength}")
	private int keyLength;
	@Value("${shortener.shortenerDomain}")
	private String shortenerDomain;
	@Value("${shortener.short.url.characters}")
	private String shortURLChars;

	public String shortenURL() {
		return shortenerDomain + "/" + generateShortURLKey();
	}

	private String generateShortURLKey() {
		Random rand = new Random();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < keyLength; i++) {
			result.append(shortURLChars.charAt(rand.nextInt(shortURLChars.length() - 1)));
		}
		return result.toString();
	}
}
