package nbcc.resto.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorApiControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ErrorApiControllerAdvice.class);

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> exceptionHandler(AuthorizationDeniedException ex, HttpServletRequest request) {
        logger.info("Access Denied on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> exceptionHandler(IllegalArgumentException ex, HttpServletRequest request) {
        logger.warn("Validation error on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> exceptionHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logger.warn("Method argument not valid on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(err -> Map.of("field", err.getField(), "message", err.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        return new ResponseEntity<>("An Unexpected Error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
