package com.example.demo.exceptions;

public class DublicateNumberException extends Exception {

    public DublicateNumberException(String msg) {
        super(msg);
    }

    public int getErrorCode() {
        return 400;
    }
}
