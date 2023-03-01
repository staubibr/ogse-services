package com.lifecycle.components.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

public class Folder {

	public String path;
	
	public Folder(String path) {
		this.path = path;
	}

	public Folder(String first, String ...more) {
		this(Paths.get(first, more).toString());
	}

	public Folder write(String name, byte[] content) throws IOException {
		Path p = Paths.get(this.path, name);

		Files.write(p, content);

		return this;
	}

	public Folder write(String name, String content) throws IOException {
		return this.write(name, content.getBytes());
	}

	public Folder create(String name) throws IOException {
		Path p = Paths.get(this.path, name);
		File f = new File(p.toString());

		if (f.exists()) throw new IOException("Cannot create folder " + name + ", it already exists.");

		if (!f.mkdirs()) throw new IOException("Unable to create folder " + name + ".");

		return new Folder(p.toString());
	}

	public Folder create_uuid() throws IOException {
		return this.create(this.get_uuid().toString());
	}

	public UUID get_uuid() {
		UUID uuid;

		do uuid = UUID.randomUUID();

		while (this.exists(uuid.toString()));

		return uuid;
	}

	public boolean exists(String... file_name) {
		File f = Paths.get(this.path, file_name).toFile();

		return f.exists();
	}

	public void copy(InputStream f, String file_name) throws IOException {
		java.nio.file.Files.copy(f, Paths.get(path, file_name), StandardCopyOption.REPLACE_EXISTING);

		f.close();
	}

	public void copy(MultipartFile f) throws IOException {
		this.copy(f.getInputStream(), f.getOriginalFilename());
	}

	public void copy(File f) throws IOException {
		this.copy(new FileInputStream(f), f.getName());
	}

	public void copy(MultipartFile f, String file_name) throws IOException {
		this.copy(f.getInputStream(), file_name);
	}

	public void copy(File f, String file_name) throws IOException {
		this.copy(new FileInputStream(f), file_name);
	}

	public void delete(String... file_names) throws IOException {
		File f = Paths.get(this.path, file_names).toFile();

		FileSystemUtils.deleteRecursively(Objects.requireNonNull(f, "Folder requested does not exist."));
	}

	public void delete() throws IOException {
		File f = Paths.get(this.path).toFile();

		FileSystemUtils.deleteRecursively(Objects.requireNonNull(f, "Folder requested does not exist."));
	}

	public void empty(String... file_names) throws IOException {
		File f = Paths.get(this.path, file_names).toFile();

		FileUtils.cleanDirectory(f);
	}

	public void empty() throws IOException {
		File f = Paths.get(this.path).toFile();

		FileUtils.cleanDirectory(f);
	}

	public List<File> files() {
		File f = Paths.get(this.path).toFile();

		return new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.listFiles(), "Folder requested does not exist.")));
	}

	public List<File> files(String ... file_names) {
		File f = Paths.get(this.path, file_names).toFile();

		return new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.listFiles(), "Folder requested does not exist.")));
	}

	public File file(String... file_name) throws Exception {
		File f = Paths.get(this.path, file_name).toFile();

		return Objects.requireNonNull(f, "File requested does not exist.");
	}
	public Folder folder(String... folder_name) throws Exception {
		return new Folder(Paths.get(this.path, folder_name).toString());
	}

	public byte[] zip(List<File> files) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		for (File file : files) {
			FileInputStream fis = new FileInputStream(file);
			ZipEntry entry = new ZipEntry(file.getName());

			zos.putNextEntry(entry);
			zos.write(fis.readAllBytes());
			zos.closeEntry();
		}

		zos.close();

		return baos.toByteArray();
	}

	public byte[] zip() throws IOException {
		return this.zip(this.files());
	}
}
