package techquack.com.onestar3gram.exceptions.post;

public class PostInvalidException extends RuntimeException {

    public PostInvalidException() {
        super("Post not found");
    }
}
