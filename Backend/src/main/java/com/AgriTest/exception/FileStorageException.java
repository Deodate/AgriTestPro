// File: src/main/java/com/AgriTest/exception/FileStorageException.java
package com.AgriTest.exception;

public class FileStorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}