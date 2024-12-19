package techquack.com.onestar3gram.services;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminClientService {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak-realm-name}")
    private String realmName;

    public List<UserRepresentation> searchByKeycloakId(String keycloakId) {
        List<UserRepresentation> allUsers = keycloak.realm(realmName)
                .users()
                .list();

        return allUsers.stream()
                .filter(user -> user.getId().equals(keycloakId))
                .collect(Collectors.toList());
    }
}
