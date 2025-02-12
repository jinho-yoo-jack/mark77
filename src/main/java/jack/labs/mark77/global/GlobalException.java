package jack.labs.mark77.global;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, IOException.class, UnexpectedTypeException.class})
    public ResponseEntity<ErrorResponse> handlerAllException(Exception e, HttpServletRequest request) throws IOException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(500, "Internal Server Error", makeRequestMessage((WebRequest) request), request.getRequestURI()));
    }

    @Override
    @Order(1)
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpHeaders headers,
                                                                            HttpStatusCode status,
                                                                            WebRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of(500, "Internal Server Error", makeRequestMessage(request), ((HttpServletRequest) request).getRequestURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeRequestMessage(WebRequest request) throws IOException {
        return ((HttpServletRequest) request).getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
