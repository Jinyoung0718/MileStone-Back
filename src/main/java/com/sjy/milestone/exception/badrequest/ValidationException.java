package com.sjy.milestone.exception.badrequest;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Map;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BadRequest  {
    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("검증 실패");
        this.errors = errors;
    }
}