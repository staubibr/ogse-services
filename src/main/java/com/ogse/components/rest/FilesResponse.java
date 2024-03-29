package com.ogse.components.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class FilesResponse {
	
	public static ResponseEntity<byte[]> build(String name, byte[] content) {
    	ContentDisposition disposition = ContentDisposition.attachment().filename(name).build();
    	    	
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, disposition.toString());
		
        return ResponseEntity.ok().headers(httpHeaders).body(content);
	}

	public static ResponseEntity<byte[]> build(File file) throws IOException {
		byte[] content = Files.readAllBytes(file.toPath());

		return build(file.getName(), content);
	}
}
