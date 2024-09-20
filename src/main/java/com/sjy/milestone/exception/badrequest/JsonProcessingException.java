package com.sjy.milestone.exception.badrequest;

import org.apache.coyote.BadRequestException;

public class JsonProcessingException extends BadRequest {
    public JsonProcessingException(String message) {
        super(message);
    }
}
