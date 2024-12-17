package techquack.com.onestar3gram.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(Integer userId) {
        super("User not found with ID : " + userId);
    }
}
