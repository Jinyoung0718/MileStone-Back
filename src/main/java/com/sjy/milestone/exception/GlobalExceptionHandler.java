package com.sjy.milestone.exception;

import com.sjy.milestone.exception.badrequest.*;
import com.sjy.milestone.exception.conflict.Conflict;
import com.sjy.milestone.exception.internal_servererror.*;
import com.sjy.milestone.exception.notfound.NotFoundException;
import com.sjy.milestone.exception.unauthorized.UnAuthorized;
import com.sjy.milestone.exception.unauthorized.UnauthorizedException;
import com.sjy.milestone.exception.badrequest.ValidationException;
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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleBadRequestExceptions(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<Object> handleBadRequestExceptions(BadRequest ex) {
        if (ex instanceof ValidationException validationException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationException.getErrors());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UnAuthorized.class)
    public ResponseEntity<String> handleAuthorizationExceptions(UnAuthorized ex) {
        HttpStatus status = ex instanceof UnauthorizedException ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<String> handleConflictException(Conflict ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<String> handlePaymentExceptions(InternalServerError ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        log.error("예상하지 못한 에러 발생: ", ex);
        Map<String, String> errors = new HashMap<>();
        errors.put("예상하지 못한 에러:", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
    }
}
