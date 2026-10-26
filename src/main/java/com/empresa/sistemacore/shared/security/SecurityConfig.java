package com.empresa.sistemacore.shared.security;

import java.util.Collection;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object claim = jwt.getClaims().get("roles");
            if (!(claim instanceof Collection<?> roles)) return List.of();
            return roles.stream()
                    .map(String::valueOf)
                    .filter(role -> role.equals(Roles.OPERADOR) || role.equals(Roles.SUPERVISOR) || role.equals(Roles.INTEGRACION))
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/integraciones/v1/**").hasRole(Roles.INTEGRACION)
                        .requestMatchers("/api/cierre-diario/**").hasRole(Roles.SUPERVISOR)
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(converter)));
        return http.build();
    }
}
