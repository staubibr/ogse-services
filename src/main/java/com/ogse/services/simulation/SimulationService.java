package com.ogse.services.simulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ogse.components.io.Folder;
import com.ogse.components.processes.SimulationProcess;

@Service
public class SimulationService {

	private final SimulationProcess simulator;
	private final Folder scratch;

	@Autowired
	public SimulationService(@Value("${app.process.simulation}") String simulator,
							 @Value("${app.folders.scratch}") String scratch) {
		this.scratch = new Folder(scratch);
    	this.simulator = new SimulationProcess(simulator);
	}

	public Folder Execute(InputStream config, Long iterations, Double duration) throws Exception {
		Folder f_scratch = this.scratch.create_uuid();
		File scenario = f_scratch.copy(config, "scenario.json").file("scenario.json");

		this.simulator.execute(scenario, iterations, duration, f_scratch);

		return f_scratch;
	}

	public Folder Execute(File config, Long iterations, Double duration) throws Exception {
		return this.Execute(new FileInputStream(config), iterations, duration);
	}
    
	public Folder Execute(MultipartFile config, Long iterations, Double duration) throws Exception {
		return this.Execute(config.getInputStream(), iterations, duration);
	}
}
