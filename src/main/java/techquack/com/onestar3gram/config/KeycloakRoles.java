package techquack.com.onestar3gram.config;

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
}
