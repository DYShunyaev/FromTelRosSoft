package com.DYShunyaev.TelRosSoft.exception;

public class NoSuchUsersException extends RuntimeException{
    public NoSuchUsersException() {
    }

    public NoSuchUsersException(String message) {
        super(message);
    }
}
