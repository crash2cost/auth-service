package io.github.mrlevi1112.authservice.exception;

/**
 * Exception thrown when a requested user cannot be found.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
