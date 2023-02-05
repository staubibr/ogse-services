package com.lifecycle.components.rest;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;


public class RestResponse {	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date timestamp;

    private int status;
    private String message;
    private String path;

	public RestResponse(int status, String message, String path, Date timestamp) {
		this.status = status;
		this.message = message;
		this.path = path;
		this.timestamp = timestamp;
	}
}
