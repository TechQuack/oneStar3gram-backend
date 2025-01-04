package techquack.com.onestar3gram.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   Converter<Jwt, AbstractAuthenticationToken> authenticationConverter
    ) throws Exception {
        http.oauth2ResourceServer(resourceServer -> {
            resourceServer.jwt(jwtDecoder -> {
                jwtDecoder.jwtAuthenticationConverter(authenticationConverter);
            });
        });

        http.sessionManagement(sessions -> {
            sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        http.authorizeHttpRequests(requests -> {
            requests.requestMatchers("/swagger-ui/*").permitAll();
            requests.requestMatchers("/v3/*").permitAll();
            requests.requestMatchers("/v3/*/*").permitAll();
            //AppUser:
            requests.requestMatchers(HttpMethod.GET,"/user/username/*").permitAll();
            requests.requestMatchers(HttpMethod.GET,"/user/all").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.GET,"/user/self").authenticated();
            requests.requestMatchers(HttpMethod.PUT,"/user/update").authenticated();
            requests.requestMatchers(HttpMethod.DELETE,"/user/delete/*").authenticated();
            requests.requestMatchers(HttpMethod.GET,"/user/*").permitAll();
            //Comment:
            requests.requestMatchers(HttpMethod.GET,"/post/*/comments").permitAll();
            requests.requestMatchers(HttpMethod.GET,"/comment/*/post").permitAll();
            requests.requestMatchers(HttpMethod.POST,"/post/*/comments/value").authenticated();
            requests.requestMatchers(HttpMethod.PUT,"/comment/*/value/*").authenticated();
            requests.requestMatchers(HttpMethod.PUT,"/comment/*/like").authenticated();
            requests.requestMatchers(HttpMethod.GET, "/comment/*").permitAll();
            requests.requestMatchers(HttpMethod.DELETE, "/comment/*").authenticated();
            //Image:
            requests.requestMatchers(HttpMethod.GET,"/image").permitAll();
            requests.requestMatchers(HttpMethod.GET,"/image/download/*").permitAll();
            requests.requestMatchers(HttpMethod.POST,"/image/upload").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.GET,"/image/*").permitAll();
            requests.requestMatchers(HttpMethod.DELETE, "/image/*").hasAuthority(KeycloakRoles.ADMIN.getRole());
            //Post:
            requests.requestMatchers(HttpMethod.GET,"/post").permitAll();
            requests.requestMatchers(HttpMethod.GET,"/post/author/*").permitAll();
            requests.requestMatchers(HttpMethod.POST,"/post").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.PUT,"/post/*").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.DELETE, "/post/*").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.PUT, "/post/like/*").authenticated();
            requests.requestMatchers(HttpMethod.GET,"/post/*").permitAll();
            //Video:
            requests.requestMatchers(HttpMethod.GET, "/video").permitAll();
            requests.requestMatchers(HttpMethod.GET, "/video/download/*").permitAll();
            requests.requestMatchers(HttpMethod.POST,"/video/upload").hasAuthority(KeycloakRoles.ADMIN.getRole());
            requests.requestMatchers(HttpMethod.GET, "/uploads/*").permitAll();
            requests.requestMatchers(HttpMethod.GET,"/video/*").permitAll();
            requests.requestMatchers(HttpMethod.DELETE, "/video/*").hasAuthority(KeycloakRoles.ADMIN.getRole());
        });
        return http.build();
    }

    @Bean
    JwtAuthenticationConverter authenticationConverter(
            Converter<Map<String, Object>, Collection<GrantedAuthority>> authoritiesConverter) {
        var authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> authoritiesConverter.convert(jwt.getClaims()));
        return authenticationConverter;
    }

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
            var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
            return roles.stream().flatMap(Collection::stream)
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        };
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://proxy-onestar3gram"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
