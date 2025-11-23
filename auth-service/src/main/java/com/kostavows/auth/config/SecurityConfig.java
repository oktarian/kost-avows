package com.kostavows.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger UI & OpenAPI docs bebas akses
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/swagger-ui.html", 
                    "/v3/api-docs/**", 
                    "/swagger-resources/**", 
                    "/webjars/**"
                ).permitAll()

                // Endpoint register & login bebas akses (auth-service)
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()

                // Semua endpoint transaction-service bebas akses (karena pakai JWT dari header)
                .requestMatchers("/api/transactions/**").permitAll()

                // Root & actuator (kalau ada) bebas akses
                .requestMatchers("/", "/actuator/**").permitAll()

                // Sisanya butuh autentikasi (JWT akan dicek manual di filter)
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form.disable());

        return http.build();
    }
}