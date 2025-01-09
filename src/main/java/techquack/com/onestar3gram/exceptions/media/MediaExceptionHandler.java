package techquack.com.onestar3gram.exceptions.media;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import techquack.com.onestar3gram.entities.ErrorEntity;
import techquack.com.onestar3gram.exceptions.post.PostInvalidException;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class MediaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorEntity> storageExceptionHandler(StorageException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(error);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorEntity> fileNotFoundHandler(FileNotFoundException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }
}