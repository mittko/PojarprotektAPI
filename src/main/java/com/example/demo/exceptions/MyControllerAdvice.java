package com.example.demo.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.json.JSONParserConstants;
import org.hibernate.TypeMismatchException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.HashMap;

@Component
@ControllerAdvice
public class MyControllerAdvice {

 /*   Class DefaultHandlerExceptionResolver
    The default implementation of the HandlerExceptionResolver interface, resolving standard Spring MVC exceptions and translating them to corresponding HTTP status codes.
    This exception resolver is enabled by default in the common Spring DispatcherServlet.

            Supported Exceptions
    Exception	HTTP Status Code
    HttpRequestMethodNotSupportedException
405 (SC_METHOD_NOT_ALLOWED)
    HttpMediaTypeNotSupportedException
415 (SC_UNSUPPORTED_MEDIA_TYPE)
    HttpMediaTypeNotAcceptableException
406 (SC_NOT_ACCEPTABLE)
    MissingPathVariableException
500 (SC_INTERNAL_SERVER_ERROR)
    MissingServletRequestParameterException
400 (SC_BAD_REQUEST)
    MissingServletRequestPartException
400 (SC_BAD_REQUEST)
    ServletRequestBindingException
400 (SC_BAD_REQUEST)
    ConversionNotSupportedException
500 (SC_INTERNAL_SERVER_ERROR)
    TypeMismatchException
400 (SC_BAD_REQUEST)
    HttpMessageNotReadableException
400 (SC_BAD_REQUEST)
    HttpMessageNotWritableException
500 (SC_INTERNAL_SERVER_ERROR)
    MethodArgumentNotValidException
400 (SC_BAD_REQUEST)
    MethodValidationException
500 (SC_INTERNAL_SERVER_ERROR)
    HandlerMethodValidationException
400 (SC_BAD_REQUEST)
    NoHandlerFoundException
404 (SC_NOT_FOUND)
    NoResourceFoundException
404 (SC_NOT_FOUND)
    AsyncRequestTimeoutException
503 (SC_SERVICE_UNAVAILABLE)
    AsyncRequestNotUsableException*/




    @ExceptionHandler( {SQLException.class, DublicateNumberException.class} )
    public ResponseEntity<HashMap<String,String>> handleException(final Exception exception,
                                                              final HttpServletRequest request) {
        exception.printStackTrace();

        HashMap<String,String> msg = new HashMap<>();
        msg.put("error",exception.getMessage());

        if(exception instanceof SQLException) {
            String state = ((SQLException)exception).getSQLState();
            if(state.equals("23505")) {
                msg.put("error","Дублиране на данни");
                return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST);
            }
        }
        if(exception instanceof DublicateNumberException) {
            msg.put("error code",String.valueOf((((DublicateNumberException)exception).getErrorCode())));
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }




        // get error code and accordingly this code return appropriate http status
        msg.put("error code",String.valueOf((((SQLException)exception).getErrorCode())));

        return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
