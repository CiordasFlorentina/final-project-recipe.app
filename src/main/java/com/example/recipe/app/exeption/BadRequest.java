package com.example.recipe.app.exeption;

public class BadRequest extends RuntimeException {
    public BadRequest(String filedName) {
        super(filedName);
    }
}
