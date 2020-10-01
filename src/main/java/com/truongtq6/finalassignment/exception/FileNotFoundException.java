package com.truongtq6.finalassignment.exception;

public class FileNotFoundException extends RuntimeException{

    private static final long serialVersionUID = -3128681006635769412L;

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
}
