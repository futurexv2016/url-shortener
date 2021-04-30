package com.bondle.shortenurl.config;

import com.bondle.shortenurl.api.APILoggingFilter;
import org.springframework.context.annotation.*;

@Configuration
public class LogConfig {
    @Bean
    public APILoggingFilter loggingFilter() {
        return new APILoggingFilter();
    }
}
