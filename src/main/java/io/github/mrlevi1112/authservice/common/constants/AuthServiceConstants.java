package io.github.mrlevi1112.authservice.common.constants;

public class AuthServiceConstants {
    private AuthServiceConstants() {}
    
    public static class Validation{
        private Validation() {}
        public static final String INVALID_EMAIL = "Email not valid";
        public static final String BLANK_EMAIL = "Email cannot be blank";
        public static final String INVALID_USERNAME = "Username not valid";
        public static final String NULL_USERNAME = "Username cannot be null";
        public static final String BLANK_USERNAME = "Username cannot be blank";
        public static final String BLANK_PASSWORD = "password cannot be blank";
        public static final String NULL_PASSWORD = "password cannot be null";
        public static final int MIN_PASSWORD_LENGTH = 6;
        public static final int MAX_PASSWORD_LENGTH = 16;
        public static final String  PASSWORD_REGEXP = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).*$";
        public static final String PASSWORD_REQUIREMENTS = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character";
        public static final String PASSWORD_LENGTH_MESSAGE = "Password must be at least " + MIN_PASSWORD_LENGTH + " characters long and at most " + MAX_PASSWORD_LENGTH + " characters long";
        public static final String NULL_USER_ROLE = "User role cannot be null";
        public static final String USERNAME_LENGTH = "Username must be between 2 and 32 characters long";
        public static final int MIN_USERNAME_LENGTH = 2;
        public static final int MAX_USERNAME_LENGTH = 32;
    }
    public static class Database{
        private Database() {}
        
        public static final String USERS_COLLECTION = "users";
        public static final String DATABSE_NAME = "crash2cost";
        public static final String MONGO_REPOSITORY_PACKAGE = "io.github.mrlevi1112.authservice.repository";
        public static final String MONGO_CLIENT_CONNECTION = "${MONGO_CLIENT_CONNECTION}";
    }
    public static class Security{
        private Security() {}
        
        public static final String INVALID_CREDENTIALS = "Invalid username or password";
        public static final String USERNAME_NOT_FOUND = "User not found with username: ";
        public static final String USER_ROLE = "ROLE_";
        public static final int AUTH_HEADER_PREFIX_LENGTH = 7;
        public static final String AUTH_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String LOGGER_ERROR = "Cannot set user authentication: {}";
        public static final String AUTH_ENDPOINT_PATTERN = "/api/auth/**";
        public static final String AUTH_API_BASE = "/api/auth/";
        public static final String SIGNUP_ENDPOINT = "/signup";
        public static final String LOGIN_ENDPOINT = "/login";
        public static final int HTTP_UNAUTHORIZED = 401;
        public static final String ROLE_KEY = "role";
    }
    public static class AuthMessages{
        private AuthMessages() {}
        
        public static final String USERNAME_EXISTS = "Username already exists";
        public static final String EMAIL_EXISTS = "Email already exists";
        public static final String USERNAME_NOT_FOUND = "Username not found";
        public static final String MESSAGE = "message";
    }

    public static class Jwt{
        private Jwt() {}
        
        public static final String JWT_KEY = "${jwt.secret}";
        public static final String JWT_EXPIRATION = "${jwt.expiration}";
    }

}