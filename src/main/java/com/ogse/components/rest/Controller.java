package com.ogse.components.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class Controller {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> handleException(Exception e, HttpServletRequest r) {
        e.printStackTrace();

		RestResponse error = new RestResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
