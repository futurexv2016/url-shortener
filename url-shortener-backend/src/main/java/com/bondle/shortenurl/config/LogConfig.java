package com.bondle.shortenurl.config;

import com.bondle.shortenurl.api.LoggingFilter;
import org.springframework.context.annotation.*;

@Configuration
public class LogConfig {
    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
