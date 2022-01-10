package com.example.recipe.app.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {
    String message;
    Integer status;
    String error;

    ApiError(String message, Integer status) {
        this.message = message;
        this.status = status;
        error = HttpStatus.BAD_REQUEST.getReasonPhrase();
    }
}
