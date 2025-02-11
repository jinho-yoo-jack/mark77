package jack.labs.mark77.global;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public String allExceptionHandler(Exception e) {
        return "Error Message ::: " + e.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    public String runTimeExceptionHandler(Exception e) {
        return "Error Message ::: " + e.getMessage();
    }
}
