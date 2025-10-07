package com.example.swp.Controller;

import com.example.swp.Config.JwtUtil;
import com.example.swp.DTO.response.PageResponseDTO;
import com.example.swp.DTO.response.TFUResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseAPIController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected JwtUtil jwtUtil;


    protected <T> ResponseEntity<TFUResponse<T>> success(T data, String message) {
        TFUResponse<T> body = new TFUResponse<>(
                true, data, message, HttpStatus.OK.value(), null
        );
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<TFUResponse<T>> success(T data) {
        return success(data, "Success");
    }

    protected <T> ResponseEntity<TFUResponse<T>> result(boolean status) {
        if( status) {
            return success(null, "Successful");
        } else {
            return error( "Some thing wrong", HttpStatus.BAD_REQUEST);
        }
    }


    protected <T> ResponseEntity<TFUResponse<T>> error(
            String message, HttpStatus status
    ) {
        TFUResponse<T> body = new TFUResponse<>(
                false, null, message, status.value(), null
        );
        return ResponseEntity.status(status).body(body);
    }



    protected <T> ResponseEntity<TFUResponse<T>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    protected <T> ResponseEntity<TFUResponse<T>> unauthorized(String message) {
        return error(message, HttpStatus.UNAUTHORIZED);
    }

    protected <T> ResponseEntity<TFUResponse<T>> forbidden(String message) {
        return error(message, HttpStatus.FORBIDDEN);
    }

    protected <T> ResponseEntity<TFUResponse<T>> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    protected <T> ResponseEntity<TFUResponse<PageResponseDTO<T>>> successPage(Page<T> page) {
        PageResponseDTO<T> pageResponse = PageResponseDTO.from(page);
        return success(pageResponse, "Data retrieved successfully");
    }

    protected Long getCurrentUserId() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserId(token);
        }
        return null;
    }

}
