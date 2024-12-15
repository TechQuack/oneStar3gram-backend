package techquack.com.onestar3gram.entities;

import java.time.LocalDateTime;

public class ErrorEntity {
    private final LocalDateTime timeStamp;
    private String message;
    private int httpStatus;

    public ErrorEntity(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public LocalDateTime getTimeStamp() {
        return this.timeStamp;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatus() {
        return this.httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
