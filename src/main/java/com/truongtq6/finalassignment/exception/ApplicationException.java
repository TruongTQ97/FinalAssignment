package com.truongtq6.finalassignment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class ApplicationException  extends  RuntimeException  {

    @ExceptionHandler(value={NoHandlerFoundException.class})
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)
    public AppException badRequest(Exception e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();
        return new AppException(400, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @Data
    @AllArgsConstructor@NoArgsConstructor
    private class AppException{
        private int code;
        private String message;
    }
}
