package com.project.controller.advice;

import com.project.domain.exception.*;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.DateTimeException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(NoResultException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.NOT_FOUND.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(ResourceNotFoundException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.NOT_FOUND.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exp,
                                                                                 HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleValueNotSupportedException(ValueNotSupportedException exp,
                                                                             HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleValueNotSupportedException(NullPointerException exp,
                                                                             HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleValueNotSupportedException(DateTimeException exp,
                                                                             HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleAssertionError(AssertionError exp, HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleMethodCanNotBePerformedException(MethodCanNotBePerformedException exp,
                                                                                  HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.METHOD_NOT_ALLOWED.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(SQLIntegrityConstraintViolationException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp,
                                                                                  HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleAccessDeniedException(AccessDeniedException exp,
                                                                                  HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.FORBIDDEN.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ExceptionMessage> handlePropertyReferenceException(PropertyReferenceException exp, HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

}
