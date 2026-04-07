package org.example.partie01.exercice3;

/**
 * Exception thrown when the external product API call fails
 * (network error, timeout, HTTP 5xx, etc.).
 */
public class ProductApiException extends RuntimeException {

    public ProductApiException(String message) {
        super(message);
    }

    public ProductApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
