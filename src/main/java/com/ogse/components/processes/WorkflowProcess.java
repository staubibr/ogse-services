package com.ogse.components.processes;
import com.ogse.components.io.Folder;

import java.io.File;

public class WorkflowProcess extends Process {
		
	public WorkflowProcess(String path) {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	// TODO: Experiment should be nullable
	public void execute(File workflow, File experiment, Folder output) throws Exception {
		String[] a_command = { this.tool.toString(), workflow.toString(), experiment.toString(), output.path };

		int exit = this.execute(a_command);
		
		if (exit != 0) throw new Exception("Unable to execute the workflow.");
	}
}
