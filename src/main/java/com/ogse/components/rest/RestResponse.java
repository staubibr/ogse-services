package com.ogse.components.rest;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RestResponse {
	public int status;
	public String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	public Date timestamp;

	public RestResponse(int status, String message, Date timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

	public RestResponse(int status, String message) {
		this(status, message, new Date());
	}
}
