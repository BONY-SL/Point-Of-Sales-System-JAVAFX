package org.pos.project.possystem.exception;

public class UserNotFound extends RuntimeException {

    private String message;

    public UserNotFound(String message, String message1) {
        super(message);
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
