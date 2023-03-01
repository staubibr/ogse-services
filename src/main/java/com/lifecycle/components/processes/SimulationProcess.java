package com.lifecycle.components.processes;
import com.lifecycle.components.io.Folder;
import java.io.File;

public class SimulationProcess extends Process {

	public SimulationProcess(String path) {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	public void execute(Folder scratch, Long iterations, Double duration) throws Exception {
		int exit;

		if (iterations != null) exit = this.execute(this.tool.toString(), scratch.file("scenario.json").toString(), scratch.path, iterations.toString());

		else if (duration != null) exit = this.execute(this.tool.toString(), scratch.file("scenario.json").toString(), scratch.path, duration.toString());
		
		else exit = this.execute(this.tool.toString(), scratch.file("scenario.json").toString(), scratch.path);

		if (exit != 0) throw new Exception("Unable to execute the simulation.");
	}
}
