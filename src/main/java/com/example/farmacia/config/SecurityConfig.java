package com.example.farmacia.config;

import com.example.farmacia.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    @Profile("!dev") // Configuración para producción
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/health/**").permitAll()
                .requestMatchers("/api/swagger-ui/**").permitAll()
                .requestMatchers("/api/v3/api-docs/**").permitAll()
                .requestMatchers("/api/h2-console/**").permitAll()
                .requestMatchers("/api/swagger-ui.html").permitAll()
                .requestMatchers("/api/v3/api-docs").permitAll()
                
                // Endpoints que requieren autenticación
                .requestMatchers("/api/medicamentos/**").authenticated()
                .requestMatchers("/api/clientes/**").authenticated()
                .requestMatchers("/api/ventas/**").authenticated()
                .requestMatchers("/api/proveedores/**").authenticated()
                .requestMatchers("/api/alertas/**").authenticated()
                .requestMatchers("/api/reportes/**").authenticated()
                .requestMatchers("/api/nlq/**").authenticated()
                
                // Cualquier otra petición requiere autenticación
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Profile("dev") // Configuración para desarrollo - PERMITE TODO
    public SecurityFilterChain securityFilterChainDev(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // En desarrollo, permitir todos los endpoints
                .anyRequest().permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
} 