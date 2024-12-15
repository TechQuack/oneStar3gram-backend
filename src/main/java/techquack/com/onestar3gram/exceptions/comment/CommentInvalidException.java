package techquack.com.onestar3gram.exceptions.comment;

public class CommentInvalidException extends RuntimeException {

    public CommentInvalidException() {
        super("Comment not found");
    }
}
