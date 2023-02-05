package com.lifecycle.components.processes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.components.io.Folder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PythonProcessV2 extends Process {

	public PythonProcessV2(String path) throws IOException {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	public List<File> execute(Folder scratch, Folder folder, JsonNode experiment) throws Exception {
		ObjectMapper om = new ObjectMapper();
		om.writeValue(scratch.path("experiment.json").toFile(), experiment);
		Folder output = new Folder(scratch.path("output"));

		List<String> command = new ArrayList<>();

		command.add(this.tool.toString());
		command.add(folder.path("workflow.json").toString());
		command.add(scratch.path("experiment.json").toString());
		command.add(output.path.toString());

		int exit = this.execute(command);
		
		if (exit != 0) throw new Exception("Unable to execute the workflow.");
		
		return output.files().stream().filter((File f) -> !f.getName().equals("workflow.json")).collect(Collectors.toList());
	}
}
