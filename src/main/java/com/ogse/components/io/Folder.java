package com.ogse.components.io;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogse.components.workspace.Workspace;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

public class Folder {

	public Path path;
	
	public Folder(String path) {
		this.path = Paths.get(path);
	}

	public Folder(String first, String ...more) {
		this(Paths.get(first, more).toString());
	}

	public Folder write(String name, byte[] content) throws IOException {
		Path p = this.path.resolve(name);
		// Path p = Paths.get(this.path, name);

		Files.write(p, content);

		return this;
	}

	public Folder write(String name, String content) throws IOException {
		return this.write(name, content.getBytes());
	}

	public Folder write(String name, Object content) throws IOException {
		ObjectMapper om = new ObjectMapper();

		om.writeValue(this.path.resolve(name).toFile(), content);

		return this;
	}

	public <T> T read(String name, Class<T> type) throws IOException {
		Path p = this.path.resolve(name);
		ObjectMapper om = new ObjectMapper();

		return om.readValue(p.toFile(), type);
	}

	public Workspace workspace() throws IOException {
		Path p = this.path.resolve("workspace.json");
		ObjectMapper om = new ObjectMapper();

		return om.readValue(p.toFile(), Workspace.class);
	}

	public Folder create(String name) throws IOException {
		Path p = this.path.resolve(name);
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
		File f = Paths.get(this.path.toString(), file_name).toFile();

		return f.exists();
	}

	public Folder copy(InputStream f, String file_name) throws IOException {
		java.nio.file.Files.copy(f, this.path.resolve(file_name), StandardCopyOption.REPLACE_EXISTING);

		f.close();

		return this;
	}

	public Folder copy(MultipartFile f) throws IOException {
		return this.copy(f.getInputStream(), f.getOriginalFilename());
	}

	public Folder copy(File f) throws IOException {
		return this.copy(new FileInputStream(f), f.getName());
	}
	@SuppressWarnings("UnusedReturnValue")
	public Folder copy(MultipartFile f, String file_name) throws IOException {
		return this.copy(f.getInputStream(), file_name);
	}

	public Folder copy(File f, String file_name) throws IOException {
		return this.copy(new FileInputStream(f), file_name);
	}

	public Folder delete(String... file_names) throws IOException {
		File f = Paths.get(this.path.toString(), file_names).toFile();

		FileSystemUtils.deleteRecursively(Objects.requireNonNull(f, "Folder requested does not exist."));

		return this;
	}

	public Folder delete() throws IOException {
		File f = this.path.toFile();

		FileSystemUtils.deleteRecursively(Objects.requireNonNull(f, "Folder requested does not exist."));

		return this;
	}

	@SuppressWarnings("UnusedReturnValue")
	public Folder empty(String... file_names) throws IOException {
		File f = Paths.get(this.path.toString(), file_names).toFile();

		FileUtils.cleanDirectory(f);

		return this;
	}

	public Folder empty() throws IOException {
		File f = this.path.toFile();

		FileUtils.cleanDirectory(f);

		return this;
	}

	public List<File> files() {
		File f = this.path.toFile();

		return new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.listFiles(), "Folder requested does not exist.")));
	}

	public List<File> files(String ... file_names) {
		File f = Paths.get(this.path.toString(), file_names).toFile();

		return new ArrayList<>(Arrays.asList(Objects.requireNonNull(f.listFiles(), "Folder requested does not exist.")));
	}

	public File file(String... file_name) throws Exception {
		File f = Paths.get(this.path.toString(), file_name).toFile();

		return Objects.requireNonNull(f, "File requested does not exist.");
	}
	public Folder folder(String... folder_name) throws Exception {
		return new Folder(Paths.get(this.path.toString(), folder_name).toString());
	}

	public byte[] zip(List<Path> paths) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		for (Path p : paths) {
			ZipEntry entry = new ZipEntry(this.path.relativize(p).toString());

			zos.putNextEntry(entry);
			Files.copy(p, zos);
			zos.closeEntry();
		}

		zos.close();

		return baos.toByteArray();
	}

	public byte[] zip() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		List<Path> files;

		try (Stream<Path> walk = Files.walk(this.path)) {
			files = walk.filter(path -> !Files.isDirectory(path))
						.collect(Collectors.toList());
		}

		return this.zip(files);
	}
}
