package com.example.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()
            ))

            .sessionManagement(session -> session
                    .sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/api/auth/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/actuator/health"
                    ).permitAll()

                    .requestMatchers("/api/students/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER",
                            "STUDENT"
                    )

                    .requestMatchers("/api/teachers/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER"
                    )

                    .requestMatchers("/api/courses/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER"
                    )

                    .requestMatchers("/api/enrollments/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER",
                            "STUDENT"
                    )

                    .requestMatchers("/api/exams/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER"
                    )

                    .requestMatchers("/api/grades/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER",
                            "STUDENT"
                    )

                    .requestMatchers("/api/files/**")
                    .hasAnyRole(
                            "ADMIN",
                            "TEACHER",
                            "STUDENT"
                    )

                    .anyRequest().authenticated()
            )

            .exceptionHandling(exception -> exception

                    .authenticationEntryPoint(
                            (request, response, authException) -> {

                                response.setStatus(401);
                                response.setContentType(
                                        "application/json"
                                );

                                response.getWriter().write(
                                        "{\"error\":\"Unauthorized\"}"
                                );
                            }
                    )

                    .accessDeniedHandler(
                            (request, response, accessDeniedException) -> {

                                response.setStatus(403);
                                response.setContentType(
                                        "application/json"
                                );

                                response.getWriter().write(
                                        "{\"error\":\"Access Denied\"}"
                                );
                            }
                    )
            )

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000",
                        "http://localhost:4200"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}