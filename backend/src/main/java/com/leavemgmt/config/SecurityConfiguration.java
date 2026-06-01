package com.leavemgmt.config;

import com.leavemgmt.security.JwtAuthenticationFilter;
import com.leavemgmt.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * SecurityConfiguration - Configures Spring Security with JWT and role-based access control
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        
                        // Employee endpoints
                        .requestMatchers(HttpMethod.GET, "/api/employees/dashboard").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/leaves/apply").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/leaves/history").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/leaves/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/leaves/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/employees/me").hasRole("EMPLOYEE")
                        
                        // Manager endpoints
                        .requestMatchers(HttpMethod.GET, "/api/managers/dashboard").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/managers/leaves").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/managers/leaves/approve").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/managers/employees/search").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/managers/statistics").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/managers/leaves/filter").hasRole("MANAGER")
                        
                        // Any other request
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080", "*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
