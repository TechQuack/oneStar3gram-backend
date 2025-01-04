package techquack.com.onestar3gram.exceptions.post;

public class PostInvalidException extends Exception {
    public PostInvalidException(String message) {
        super(message);
    }

    public PostInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
