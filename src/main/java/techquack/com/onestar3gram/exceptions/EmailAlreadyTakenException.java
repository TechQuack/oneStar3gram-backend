package techquack.com.onestar3gram.exceptions;

public class EmailAlreadyTakenException extends Exception {
    public EmailAlreadyTakenException(String email) {
        super("User not found with email : " + email);
    }

}
