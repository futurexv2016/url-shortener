package com.bondle.shortenurl.api.exception;

import com.bondle.shortenurl.dto.ErrorResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@ControllerAdvice
public class UrlShortenExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = { RuntimeException.class })
    protected ResponseEntity<Object> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        logger.error("handleRuntimeException", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponse.builder().errorMessage(ex.getMessage()).build());
    }
}
