package techquack.com.onestar3gram.exceptions;

public class NotLoggedInException extends Exception {

    public NotLoggedInException () {
        super("Forbidden access : ");
    }

}
