package com.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    // Password encoder bean (used to hash/check passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security config for route access
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for development/testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/**",          // Allow login/register endpoints
                                "/api/v1/users/**",      // Allow user-related endpoints
                                "/swagger-ui/**",        // Allow Swagger UI
                                "/api/orders/**",
                                "/api/v1/cart/**",
                                "/api/v1/products/**",
                                "/v3/api-docs/**"        // Allow OpenAPI docs
                        ).permitAll()
                        .anyRequest().authenticated() // 🔒 Any other route requires login
                )
                .formLogin().disable(); // Disable Spring’s default login form (for custom/frontend use)

        return http.build();
    }

    // Enable CORS so React frontend (on localhost:5173) can access backend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // 🔓 Allow Vite dev server
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }
}
