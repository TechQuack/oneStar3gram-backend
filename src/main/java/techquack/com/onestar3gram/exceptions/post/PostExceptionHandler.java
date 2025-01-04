package techquack.com.onestar3gram.exceptions.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import techquack.com.onestar3gram.entities.ErrorEntity;
import java.time.LocalDateTime;

@ControllerAdvice
public class PostExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidAltException.class)
    public ResponseEntity<ErrorEntity> invalidAltHandler(PostInvalidException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }

    @ExceptionHandler(InvalidDescriptionException.class)
    public ResponseEntity<ErrorEntity> invalidDescriptionHandler(InvalidDescriptionException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
    }

    @ExceptionHandler(PostInvalidException.class)
    public ResponseEntity<ErrorEntity> unauthorizedPostHandler(PostInvalidException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
    }

    @ExceptionHandler(PostInvalidException.class)
    public ResponseEntity<ErrorEntity> postInvalidHandler(PostInvalidException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorEntity> postNotFoundHandler(PostNotFoundException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }
}