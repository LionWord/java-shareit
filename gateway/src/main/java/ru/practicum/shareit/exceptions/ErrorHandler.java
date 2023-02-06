package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessage handleRuntimeException(SQLException e) {
        log.debug("Throwing " + e.getClass().getName() + "Message: " + e.getMessage());
        return new ExceptionMessage(e.getMessage());
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessage handleNotFoundException(MethodArgumentNotValidException e) {
        log.debug("Throwing " + e.getClass().getName() + "Message: " + e.getMessage());
        return new ExceptionMessage("Failed validation: please check your input");
    }*/


}
