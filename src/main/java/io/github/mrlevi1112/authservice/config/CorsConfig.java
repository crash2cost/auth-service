package io.github.mrlevi1112.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for Auth Service.
 * 
 * NOTE: CORS is handled by the API Gateway for production.
 * When requests come through the gateway, CORS headers are already set.
 * This config disables CORS at the service level to prevent duplicate headers.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Don't add CORS headers - let API Gateway handle it
        // This prevents duplicate Access-Control-Allow-Origin headers
        configuration.setAllowedOriginPatterns(List.of());
        configuration.setAllowedMethods(List.of());
        configuration.setAllowedHeaders(List.of());
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Don't register any CORS configuration - gateway handles it
        return source;
    }
}
