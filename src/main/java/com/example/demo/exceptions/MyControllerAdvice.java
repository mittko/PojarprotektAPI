package com.example.demo.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.HashMap;

@Component
@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler( {SQLException.class} )
    public ResponseEntity<HashMap<String,String>> handleSqlException(final SQLException exception,
                                                              final HttpServletRequest request) {
        System.out.println("EXECEPTION THROWN HERE !");
        HashMap<String,String> msg = new HashMap<>();
        msg.put("error",exception.getMessage());
        msg.put("error code",String.valueOf(exception.getErrorCode()));

        // get error code and accordingly this code return appropriate http status
        return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
