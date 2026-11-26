package ru.mephi.hbs.hotel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Objects;

import static jakarta.servlet.DispatcherType.ERROR;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static ru.mephi.hbs.hotel.controller.HotelController.GET_HOTELS;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return http.sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(ERROR).permitAll()
                        .requestMatchers(GET, "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml",
                                "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers(GET, GET_HOTELS).hasRole("USER")
                        .anyRequest().denyAll())
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(authenticationConverter)))
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location:#{null}}") Resource publicKey
    ) throws IOException {
        if (publicKey == null) {
            return token -> {
                throw new BadJwtException("Не задан открытый ключ проверки JWT");
            };
        }

        RSAPublicKey rsaPublicKey;
        try (InputStream input = publicKey.getInputStream()) {
            rsaPublicKey = RsaKeyConverters.x509().convert(input);
        }

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaPublicKey).build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer("hbs-booking-service"),
                new JwtAudienceValidator("hbs-api"),
                new JwtClaimValidator<Instant>(JwtClaimNames.EXP, Objects::nonNull)
        ));
        return decoder;
    }
}
