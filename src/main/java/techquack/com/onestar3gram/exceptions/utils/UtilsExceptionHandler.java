package techquack.com.onestar3gram.exceptions.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import techquack.com.onestar3gram.entities.ErrorEntity;
import java.time.LocalDateTime;

@ControllerAdvice
public class UtilsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorEntity> badRequestHandler(BadRequestException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
    }

    @ExceptionHandler(EmptyException.class)
    public ResponseEntity<ErrorEntity> noContentHandler(EmptyException exception) {
        ErrorEntity error = new ErrorEntity(LocalDateTime.now());
        error.setHttpStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(error);
    }
}
