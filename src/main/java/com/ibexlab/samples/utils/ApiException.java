package com.ibexlab.samples.utils;

public class ApiException extends RuntimeException {
    private int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int status() {
        return status;
    }

    public String message() {
        return super.getMessage();
    }
}
