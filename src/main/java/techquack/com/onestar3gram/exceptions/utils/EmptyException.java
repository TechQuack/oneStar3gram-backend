package techquack.com.onestar3gram.exceptions.utils;

public class EmptyException extends RuntimeException {

    public EmptyException() {
        super("Value is empty or not found");
    }
}
