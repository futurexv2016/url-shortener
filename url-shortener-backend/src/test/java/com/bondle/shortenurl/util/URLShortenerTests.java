package com.bondle.shortenurl.util;

import com.bondle.shortenurl.service.URLShortener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class URLShortenerTests {

  @Value("${shortener.keyLength}")
  private int keyLength;
  @Value("${shortener.shortenerDomain}")
  private String shortenerDomain;
  @Value("${shortener.short.url.characters}")
  private String shortURLChars;

  @Autowired
  URLShortener urlShortener;

  @Test
  void generate_short_url_should_success() {
    String shortUrl = urlShortener.shortenURL();
    String shortKey = shortUrl.replace(shortenerDomain + "/", "");

    for(char c : shortKey.toCharArray()) {
      assertTrue(shortURLChars.contains(String.valueOf(c)));
    }
    assertEquals(shortKey.length(), keyLength);
    assertTrue(shortUrl.startsWith(shortenerDomain));
  }
}

