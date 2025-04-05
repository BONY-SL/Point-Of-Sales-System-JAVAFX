package org.pos.project.possystem.exception;


public class UserEmailExsist extends RuntimeException {

    private String message;

    public UserEmailExsist(String message, String message1) {
        super(message);
        this.message = message1;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
