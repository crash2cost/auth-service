package io.github.mrlevi1112.authservice.exception;

/**
 * Exception thrown when attempting to create a user that already exists.
 * This can occur when the username or email is already registered.
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
