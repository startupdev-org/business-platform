package com.platform.config;

import com.platform.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String PLATFORM_ADMIN = "PLATFORM_ADMIN";
    private static final String BUSINESS_ADMIN = "BUSINESS_ADMIN";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(mag -> mag.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        .requestMatchers("/api/health/**").permitAll()

//                        TODO: CHECK THE hasAuthority Think

                        // Employee Logic - most specific first
                        .requestMatchers(HttpMethod.POST, "/api/business/*/employee").hasRole(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/business/*/employee/**").hasRole(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/business/*/employee/**").hasRole(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/business/*/employee/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/business/*/employee").authenticated()

                        // Business Logic - general paths after
                        .requestMatchers(HttpMethod.POST, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.GET, "/api/business/**").authenticated()


                        // Businesses Logic
                        .requestMatchers(HttpMethod.GET, "/api/business/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/business/**").hasAnyAuthority(BUSINESS_ADMIN)


                        .requestMatchers("/api/business/admin/**").hasAuthority(PLATFORM_ADMIN)

                        .requestMatchers("/api/users/whoami").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/business/*/service").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/booking").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/booking/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/review/booking/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/review/business/**").authenticated()
                        .requestMatchers("/swagger-ui/**","/swagger-ui/index.html", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
