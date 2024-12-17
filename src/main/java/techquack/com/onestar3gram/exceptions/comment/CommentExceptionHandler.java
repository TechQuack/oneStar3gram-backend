package techquack.com.onestar3gram.exceptions.comment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import techquack.com.onestar3gram.entities.ErrorEntity;

import java.time.LocalDateTime;

@ControllerAdvice
public class CommentExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommentInvalidException.class)
    public ResponseEntity<ErrorEntity> commentNotFoundHandler(CommentInvalidException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }
}