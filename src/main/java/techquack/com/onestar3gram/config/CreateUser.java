package techquack.com.onestar3gram.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateUser implements CommandLineRunner {
    
    @Value("${keycloak-server-url}")
    private String serverUrl;

    @Value("${keycloak-realm-name}")
    private String realmName;

    @Value("${keycloak-master-realm-name}")
    private String masterRealmName;

    @Value("${keycloak-master-admin-username}")
    private String adminUsername;

    @Value("${keycloak-master-admin-password}")
    private String adminPassword;
    
    @Value("${keycloak-admin-username}")
    private String username;

    @Value("${keycloak-admin-password}")
    private String password;

    @Value("${keycloak-admin-email}")
    private String email;

    @Value("${keycloak-admin-first-name}")
    private String firstName;

    @Value("${keycloak-admin-last-name}")
    private String lastName;

    @Value("${keycloak-master-admin-cli}")
    private String adminCli;

    @Value("${keycloak-realm-management}")
    private String realmManagement;

    @Value("${keycloak-realm-admin}")
    private String realmAdmin;

    @Override
    public void run(String... args) {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(masterRealmName)
                .username(adminUsername)
                .password(adminPassword)
                .clientId(adminCli)
                .build();

        UserRepresentation user = getUserRepresentation();

        keycloak.realm(realmName).users().create(user);


        String userId = keycloak.realm(realmName).users()
                .search(username).stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .map(UserRepresentation::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String clientId = keycloak.realm(realmName).clients()
                .findByClientId(realmManagement).stream()
                .findFirst()
                .map(ClientRepresentation::getId)
                .orElseThrow(() -> new RuntimeException("Client realm-management not found"));

        RoleRepresentation realmAdminRole = keycloak.realm(realmName).clients()
                .get(clientId).roles().get(realmAdmin).toRepresentation();

        keycloak.realm(realmName).users().get(userId)
                .roles().clientLevel(clientId).add(List.of(realmAdminRole));

    }

    private UserRepresentation getUserRepresentation() {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);


        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setCredentials(List.of(credential));
        user.setEnabled(true);
        user.setRealmRoles(List.of(realmAdmin));
        return user;
    }
}
