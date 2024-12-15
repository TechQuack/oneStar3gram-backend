package techquack.com.onestar3gram.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String username) {
        super("User not found with username : " + username);
    }

}
