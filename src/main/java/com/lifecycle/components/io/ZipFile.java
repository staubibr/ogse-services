package com.lifecycle.components.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFile {
	
	private ZipOutputStream zos;
	private ByteArrayOutputStream baos;

	public ZipFile(List<File> files) throws IOException {
        this.Open();

		for (File file : files) {
			this.WriteFull(file.getName(), file);
		}
       
        this.Close();
	}
	
	public void Open() {
	    this.baos = new ByteArrayOutputStream();
		this.zos = new ZipOutputStream(baos);
	}
	
	public void Close() throws IOException {
		this.zos.close();
	}
	
	public void NewEntry(String name) throws IOException {
	    ZipEntry entry = new ZipEntry(name);
	    
	    this.zos.putNextEntry(entry);
	}
	
	public void CloseEntry() throws IOException {
	    this.zos.closeEntry();
	}
	
	public void Write(byte[] buffer) throws IOException {
	    this.zos.write(buffer);
	}
	
	public void WriteFull(String name, byte[] buffer) throws IOException {
		this.NewEntry(name);
		
		this.Write(buffer);
		
		this.CloseEntry();
	}
	
	public void WriteFull(String name, File source) throws IOException {
		FileInputStream fis = new FileInputStream(source);
		this.WriteFull(name, fis.readAllBytes());
		fis.close();        	
	}
	
	public byte[] toByteArray() {
		return this.baos.toByteArray();		
	}
}