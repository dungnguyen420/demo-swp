package com.example.swp.Exeption;

import com.example.swp.DTO.TFUResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TFUResponse<Void>> handleAny(Exception ex, WebRequest req) {
        log.error("[{}] {}", req.getDescription(false), ex.getMessage(), ex);
        TFUResponse<Void> body = TFUResponse.<Void>builder()
                .success(false)
                .statusCode(400)
                .message(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(body);
    }
}
