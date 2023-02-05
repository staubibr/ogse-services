package com.lifecycle.components.rest;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Controller {

	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse> handleException(Exception e, HttpServletRequest r) {
		System.err.println(e.getMessage());

		RestResponse error = new RestResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), r.getRequestURI(), new Date());

        return new ResponseEntity<RestResponse>(error, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<RestResponse> handleSuccess() {
    	ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

    	RestResponse resp = new RestResponse(HttpStatus.OK.value(), "Success", "", new Date());

        return new ResponseEntity<RestResponse>(resp, HttpStatus.OK);
    }
}
