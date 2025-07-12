// File: src/main/java/com/AgriTest/exception/ExportException.java
package com.AgriTest.exception;

public class ExportException extends RuntimeException {

    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}