package com.tktkgg.selfcontrol.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import com.tktkgg.selfcontrol.exception.ApiProblemDetailFactory;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    private final ApiProblemDetailFactory problemDetailFactory;

    public SecurityConfig(ApiProblemDetailFactory problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = 
            new HttpSessionCsrfTokenRepository();
        
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "X-CSRF-TOKEN"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        CsrfTokenRepository csrfTokenRepository
    ) throws Exception {
        http
            .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .exceptionHandling((ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    problemDetailFactory.write(
                        res,
                        problemDetailFactory.create(
                            HttpStatus.UNAUTHORIZED,
                            "UNAUTHORIZED",
                            "Authentication is required.",
                            req
                        )
                    );
                })
                .accessDeniedHandler((req, res, accessDeniedEx) -> {
                    problemDetailFactory.write(
                        res,
                        problemDetailFactory.create(
                            HttpStatus.FORBIDDEN,
                            "FORBIDDEN",
                            "You do not have permission to access this resource.",
                            req
                        )
                    );
                })
            ))
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository)
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());
        return http.build();
    }
}
