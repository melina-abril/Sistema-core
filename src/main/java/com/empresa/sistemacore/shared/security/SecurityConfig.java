package com.empresa.sistemacore.shared.security;

import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/api/health", "/actuator/health").permitAll()
                        .requestMatchers("/api/integraciones/v1/**").hasRole(Roles.INTEGRACION)
                        .requestMatchers(HttpMethod.POST, "/api/cierre-diario", "/api/cierre-diario/reproceso").hasRole(Roles.SUPERVISOR)
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resource -> resource.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object roles = jwt.getClaims().get("roles");
            if (!(roles instanceof java.util.Collection<?> values)) return java.util.List.of();
            return values.stream().map(String::valueOf)
                    .filter(role -> role.equals(Roles.OPERADOR) || role.equals(Roles.SUPERVISOR) || role.equals(Roles.INTEGRACION))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        });
        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder(@Value("${core.security.jwt-secret:}") String secret) {
        if (secret.isBlank()) {
            // Sin secreto configurado no se aceptan tokens; nunca se usa una clave de ejemplo.
            return token -> { throw new JwtException("JWT verification is not configured"); };
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) throw new IllegalStateException("JWT secret must be at least 32 bytes");
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(bytes, "HmacSHA256")).build();
    }
}
