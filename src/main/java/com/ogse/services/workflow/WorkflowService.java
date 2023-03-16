package com.ogse.services.workflow;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ogse.components.metadata.Inventory;
import com.ogse.components.metadata.Entity;
import com.ogse.components.io.Folder;
import com.ogse.components.processes.WorkflowProcess;

@Service
public class WorkflowService {

	public final Inventory inventory;
	public final Folder folder;
	private final WorkflowProcess engine;
	private final Folder scratch;

    @Autowired
	public WorkflowService(@Value("${app.list.workflows}") String inventory,
						   @Value("${app.folders.workflows}") String folder,
						   @Value("${app.folders.scratch}") String scratch,
						   @Value("${app.process.workflows}") String engine) throws IOException {
		this.inventory = new Inventory(inventory);
		this.folder = new Folder(folder);
		this.scratch = new Folder(scratch);
		this.engine = new WorkflowProcess(engine);
	}

    public Entity Create(String name, String description, MultipartFile workflow) throws Exception {
		UUID uuid = this.folder.get_uuid();
		this.folder.create(uuid.toString()).copy(workflow, "workflow.json");

		Entity entity = this.inventory.Add(new Entity(uuid, name, description));
		this.inventory.Save();

		return entity;
    }
    
    public Entity Read(String uuid) throws Exception {
		return this.inventory.Get(uuid);
    }
    
    public File ReadFile(String uuid) throws Exception {
		return this.folder.file(uuid, "workflow.json");
    }

	public Entity Update(String uuid, String name, String description, Date created, MultipartFile workflow) throws Exception {
		Entity updated = this.inventory.Update(new Entity(uuid, name, description, created));
		this.inventory.Save();

		if (workflow != null) this.folder.folder(uuid).copy(workflow, "workflow.json");

		return updated;
    }
	
    public void Delete(String uuid) throws Exception {
		this.folder.delete(uuid);
		this.inventory.Remove(uuid).Save();
    }

	public Folder Execute(String uuid, String experiment) throws Exception {
		// TODO: experiment should be nullable
		Folder f_scratch = this.scratch.create_uuid();
		File f_workflow = this.folder.file(uuid, "workflow.json");
		File f_experiment = scratch.write("experiment.json", experiment).file("experiment.json");

		this.engine.execute(f_workflow, f_experiment, f_scratch);

		return scratch;

		// TODO: Just so I don't forget, these are the function calls to publish to GeoServer.
		// TODO: Code doesn't belong here though.
		// GeoServer.post_workspace(schema);
		// GeoServer.post_data_store(schema);
		// GeoServer.post_feature_type(schema, "oil_rings");
		// GeoServer.post_feature_type(schema, "grid_cells");
		// GeoServer.delete_workspace(schema);
	}
}
