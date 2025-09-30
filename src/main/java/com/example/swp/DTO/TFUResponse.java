package com.example.swp.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TFUResponse<T> {
    private Boolean success;
    private T data;
    private String message;
    private Integer statusCode;
    private Map<String, String> errors;

    public TFUResponse() {}

    public TFUResponse(Boolean success, T data, String message, Integer statusCode, Map<String, String> errors) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;
    }

    public Boolean getSuccess() { return success; }
    public void setSuccess(Boolean success) { this.success = success; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Integer getStatusCode() { return statusCode; }
    public void setStatusCode(Integer statusCode) { this.statusCode = statusCode; }
    public Map<String, String> getErrors() { return errors; }
    public void setErrors(Map<String, String> errors) { this.errors = errors; }
}
