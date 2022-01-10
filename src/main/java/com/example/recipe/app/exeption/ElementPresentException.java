package com.example.recipe.app.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ElementPresentException extends RuntimeException {

    public ElementPresentException(String filedName) {
        super(filedName);
    }
}
