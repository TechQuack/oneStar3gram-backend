package techquack.com.onestar3gram.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public enum KeycloakRoles {
    ADMIN("Admin"),
    PRIVILEGED("Privileged");

    private final String role;

    KeycloakRoles(String roleName) {
        role = roleName;
    }

    public String getRole() {
        return role;
    }

    public static boolean hasRole(KeycloakRoles role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals(role.getRole()));
    }
}
