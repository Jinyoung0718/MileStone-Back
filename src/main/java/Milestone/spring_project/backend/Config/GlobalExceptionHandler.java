package Milestone.spring_project.backend.Config;

import Milestone.spring_project.backend.Exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({
            NotFindMember.class,
            AddressNotFoundException.class,
            BoardNotFoundException.class,
            CommentNotFoundException.class,
            ProductOptionNotFoundException.class,
            CartItemNotFoundException.class,
            ProductNotFoundException.class,
            ReviewNotFoundException.class,
            OrderNotFoundException.class,
            SessionNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({
            InvalidPasswordException.class,
            InsufficientStockException.class,
            CartItemAlreadyExistsException.class,
            BadRequestEmailException.class
    })
    public ResponseEntity<String> handleBadRequestExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler({
            UnauthorizedException.class,
            AccountDeactivatedException.class
    })
    public ResponseEntity<String> handleAuthorizationExceptions(RuntimeException ex) {
        HttpStatus status = ex instanceof UnauthorizedException ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleConflictException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({
            PaymentPreparationException.class,
            PaymentVerificationException.class,
            PaymentCancellationException.class,
            WebSocketMessageException.class
    })
    public ResponseEntity<String> handlePaymentExceptions(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getErrors());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        log.error("예상하지 못한 에러 발생: ", ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("예상하지 못한 에러:", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
