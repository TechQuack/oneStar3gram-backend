package techquack.com.onestar3gram.exceptions.comment;

public class CommentInvalidException extends Exception {
    public CommentInvalidException(String message) {
        super(message);
    }

    public CommentInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
