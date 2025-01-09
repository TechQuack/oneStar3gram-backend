package techquack.com.onestar3gram.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak-realm-name}")
    private String realmName;

    @Value("${keycloak-client-id}")
    private String clientId;

    @Value("${keycloak-admin-username}")
    private String username;

    @Value("${keycloak-admin-password}")
    private String password;

    @Value("${keycloak-server-url}")
    private String serverUrl;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmName)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();
    }
}
