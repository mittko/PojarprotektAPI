package com.example.demo.exceptions;

import org.aspectj.weaver.ast.Not;

public class NotFoundException extends Exception {

    public NotFoundException(String msg) {
        super(msg);
    }
    public int getErrorCode() {
        return 404;
    }
}
