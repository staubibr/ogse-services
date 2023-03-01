package com.lifecycle.components.processes;
import com.lifecycle.components.io.Folder;
import java.io.File;

public class WorkflowProcess extends Process {
		
	public WorkflowProcess(String path) {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	public void execute(Folder scratch) throws Exception {
		String[] a_command = { this.tool.toString(), scratch.file("workflow.json").toString(), scratch.file("experiment.json").toString(), scratch.path };

		int exit = this.execute(a_command);
		
		if (exit != 0) throw new Exception("Unable to execute the workflow.");
	}
}
