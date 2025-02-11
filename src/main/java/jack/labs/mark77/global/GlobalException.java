package jack.labs.mark77.global;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public String allExceptionHandler(Exception e) {
        return e.getMessage();
    }
}
