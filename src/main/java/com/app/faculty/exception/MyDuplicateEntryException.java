package com.app.faculty.exception;

public class MyDuplicateEntryException extends RuntimeException {
    public MyDuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}
