package com.example.recipe.app.exeption;

public class ServerException extends RuntimeException {
    public ServerException(String filedName) {
        super(filedName);
    }
}