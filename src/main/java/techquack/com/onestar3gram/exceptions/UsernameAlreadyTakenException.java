package techquack.com.onestar3gram.exceptions;

public class UsernameAlreadyTakenException extends Exception {
    public UsernameAlreadyTakenException(String username) { super("User not found with username : " + username);}
}
