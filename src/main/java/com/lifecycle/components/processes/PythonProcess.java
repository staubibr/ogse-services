package com.lifecycle.components.processes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.components.io.Folder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PythonProcess extends Process {
		
	public PythonProcess(String path) throws IOException {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	public List<File> execute(Folder scratch, Folder folder, JsonNode params) throws Exception {
		Path workflow = folder.path("workflow.json");
		Path experiment = scratch.path("experiment.json");

		if (params != null) {
			ObjectMapper om = new ObjectMapper();
		    om.writeValue(experiment.toFile(), params);
		}

		String[] a_command = { this.tool.toString(), workflow.toString(), experiment.toString(), scratch.path.toString() };

		int exit = this.execute(a_command);
		
		if (exit != 0) throw new Exception("Unable to execute the workflow.");

		return scratch.files().stream().filter((File f) -> !f.getName().equals("workflow.json")).collect(Collectors.toList());
	}
}
