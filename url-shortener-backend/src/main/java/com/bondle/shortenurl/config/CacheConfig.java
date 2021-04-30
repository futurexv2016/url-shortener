package com.bondle.shortenurl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Contains configuration for cache of urls
 *
 * @author david.ho
 */
@Configuration
@EnableCaching
public class CacheConfig {

  @Value("${cache.maxSize}")
  private int maxCacheSize;

  @Bean
  public Caffeine caffeineConfig() {
    // To clean up cache if records is big over time
    return Caffeine.newBuilder().maximumSize(maxCacheSize);
  }

  @Bean
  public CacheManager cacheManager(Caffeine caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    return caffeineCacheManager;
  }
}
