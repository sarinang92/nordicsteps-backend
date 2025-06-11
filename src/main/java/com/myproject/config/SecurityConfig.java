package com.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    // This bean handles password encryption using BCrypt, which is a secure way to store passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Applies to all endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // These endpoints can be accessed by anyone, no login required
                                "/api/auth/**",
                                "/api/v1/users/**",
                                "/swagger-ui/**",
                                "/api/orders/**",
                                "/api/v1/cart/**",
                                "/api/v1/products/**",
                                "/v3/api-docs/**"

                        ).permitAll()
                        .anyRequest().authenticated() // Everything else needs the user to be logged in
                )
                .csrf(csrf -> csrf.disable()) // Disables CSRF protection
                .formLogin(Customizer.withDefaults()); // Enables default login form

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Apply CORS settings to all paths
                        .allowedOriginPatterns("http://localhost:*") // Allow requests from localhost (any port)
                        .allowedMethods("*") // Allow all HTTP methods like GET, POST, etc.
                        .allowedHeaders("*");
            }
        };
    }
}
