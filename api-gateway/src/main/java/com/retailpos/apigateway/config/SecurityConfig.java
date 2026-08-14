package com.retailpos.apigateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.retailpos.apigateway.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/auth/login")
                .permitAll()

                .requestMatchers("/auth/register")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/products/**", "/inventory/**")
                .hasAnyRole("ADMIN", "MANAGER", "CASHIER")

                .requestMatchers(HttpMethod.POST, "/products/**", "/inventory/**")
                .hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.PUT, "/products/**", "/inventory/**")
                .hasAnyRole("ADMIN", "MANAGER")

                .requestMatchers(HttpMethod.DELETE, "/products/**", "/inventory/**")
                .hasRole("ADMIN")
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}