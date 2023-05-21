package com.github.goodfatcat.mangatelegrambot.exception;

public class NoSuchUserException extends RuntimeException{
    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException() {
    }
}
