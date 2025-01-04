package techquack.com.onestar3gram.exceptions.media;

public class FileNotFoundException extends Exception {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}