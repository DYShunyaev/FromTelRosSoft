package com.DYShunyaev.TelRosSoft.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsersGlobalExceptionHandler {
    /**
     * @handlerException(): Принимает Exceptions, отправляя статус "Not Found."**/
    @ExceptionHandler
    private ResponseEntity<UsersIncorrectData> handlerException(NoSuchUsersException exception) {
        UsersIncorrectData data = new UsersIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UsersIncorrectData> handlerException(Exception exception) {
        UsersIncorrectData data = new UsersIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
