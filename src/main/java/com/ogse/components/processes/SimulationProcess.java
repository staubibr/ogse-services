package com.ogse.components.processes;
import com.ogse.components.io.Folder;
import java.io.File;

public class SimulationProcess extends Process {

	public SimulationProcess(String path) {
		super();
		
		this.tool = new File(path);
		this.workspace = new Folder(tool.getParent());		
	}

	public void execute(File scenario, Long iterations, Double duration, Folder output) throws Exception {
		int exit;

		if (iterations != null) exit = this.execute(this.tool.toString(), scenario.toString(), output.path, iterations.toString());

		else if (duration != null) exit = this.execute(this.tool.toString(), scenario.toString(), output.path, duration.toString());
		
		else exit = this.execute(this.tool.toString(), scenario.toString(), output.path);

		if (exit != 0) throw new Exception("Unable to execute the simulation.");
	}
}
