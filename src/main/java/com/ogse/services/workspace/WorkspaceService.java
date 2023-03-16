package com.ogse.services.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.ogse.components.processes.SimulationProcess;
import com.ogse.components.processes.WorkflowProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ogse.components.metadata.Inventory;
import com.ogse.components.metadata.Entity;
import com.ogse.components.io.Folder;

@Service
public class WorkspaceService {

	public final Inventory inventory;
	public final Folder workspace_folder;
	public final Folder workflow_folder;
	private final WorkflowProcess engine;
	private final SimulationProcess simulator;

    @Autowired
	public WorkspaceService(@Value("${app.list.workspaces}") String inventory,
							@Value("${app.folders.workspaces}") String workspace_folder,
							@Value("${app.folders.workflows}") String workflow_folder,
							@Value("${app.process.workflows}") String engine,
							@Value("${app.process.simulation}") String simulator) throws IOException {
		this.inventory = new Inventory(inventory);
		this.workspace_folder = new Folder(workspace_folder);
		this.workflow_folder = new Folder(workflow_folder);
		this.engine = new WorkflowProcess(engine);
		this.simulator = new SimulationProcess(simulator);
	}

    public Entity Create(String name, String description, MultipartFile workspace) throws Exception {
		UUID uuid = this.workspace_folder.get_uuid();
		this.workspace_folder.create(uuid.toString()).copy(workspace, "workspace.json");

		Entity entity = this.inventory.Add(new Entity(uuid, name, description));
		this.inventory.Save();

		return entity;
    }

    public Entity Read(String uuid) throws Exception {
		return inventory.Get(uuid);
    }

    public byte[] ReadFiles(String uuid) throws Exception {
    	return this.workspace_folder.folder(uuid).zip();
    }
    
    public Entity Update(String uuid, String name, String description, Date created, List<MultipartFile> files) throws Exception {
		Entity updated = this.inventory.Update(new Entity(uuid, name, description, created));
		this.inventory.Save();
		this.workspace_folder.empty(uuid);

		for (MultipartFile f: files) this.workspace_folder.folder(uuid).copy(f, f.getOriginalFilename());

    	return updated;
    }
	
    public void Delete(String uuid) throws Exception {
		this.workspace_folder.delete(uuid);
		this.inventory.Remove(uuid).Save();
    }

	public void ExecuteWorkflow(String workspace_uuid, String workflow_uuid, String experiment) throws Exception {
		// TODO: experiment should be nullable
		Folder f_workspace = this.workspace_folder.folder(workspace_uuid);
		File f_workflow = this.workflow_folder.file(workflow_uuid, "workflow.json");
		File f_experiment = f_workspace.write("experiment.json", experiment).file("experiment.json");

		this.engine.execute(f_workflow, f_experiment, f_workspace);
	}

	public void ExecuteSimulation(String workspace_uuid, Long iterations, Double duration) throws Exception {
		Folder f_workspace = this.workspace_folder.folder(workspace_uuid);
		File scenario = f_workspace.file("scenario.json");

		this.simulator.execute(scenario, iterations, duration, f_workspace);
	}
}
