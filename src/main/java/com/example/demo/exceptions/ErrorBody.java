package com.example.demo.exceptions;

public class ErrorBody {
    String status;
    String timestamp;
    String error;
    String path;


    public ErrorBody(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }


}