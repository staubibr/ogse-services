package com.lifecycle.services.workflow;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.entities.metadata.Entities;
import com.lifecycle.entities.metadata.Entity;
import com.lifecycle.components.io.Folder;
import com.lifecycle.components.io.UuidFolder;
import com.lifecycle.components.io.ZipFile;
import com.lifecycle.components.processes.PythonProcess;

@Service
public class WorkflowService {

	@Value("${app.process.workflows}")
	private String PYTHON_POSTGRES;

	@Value("${app.folders.workflows}")
	private String APP_FOLDERS_WORKFLOWS;
	
	@Value("${app.folders.scratch}")
	private String APP_FOLDERS_SCRATCH;

	@Value("${app.list.workflows}")
	private String APP_WORKFLOWS;

    @Autowired
	public WorkflowService() {

	}
	
    public Entities<Entity> Entities() throws IOException {
    	return new Entities<>(APP_WORKFLOWS, Entity.class);
    }
	
    public File List() throws IOException {
    	return new File(APP_WORKFLOWS);
    }
        
    public Entity Create(String name, String description, MultipartFile workflow, List<MultipartFile> data) throws Exception {
    	Entities<Entity> workflows = new Entities<>(APP_WORKFLOWS, Entity.class);
		UuidFolder scratch = new UuidFolder(APP_FOLDERS_WORKFLOWS);
		Entity entity = workflows.Add(new Entity(scratch.uuid, name, description));
		
		workflows.Save();
		scratch.copy(workflow, "workflow.json");
		
		Folder data_folder = new Folder(scratch.path("data"));
		
		for (MultipartFile d: data) data_folder.copy(d);
		
		return entity;
    }
    
    public Entity Read(String uuid) throws Exception {
		Entities<Entity> workflows = Entities(); 
    	
		return workflows.Get((e) -> e.getUuid().toString().equals(uuid));
    }
    
    public File ReadFile(String uuid) throws Exception {
    	Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);
    	
    	return folder.file("workflow.json");
    }

	public Entity Update(String uuid, String name, String description, Date created, MultipartFile workflow, List<MultipartFile> data) throws Exception {
		Entities<Entity> workflows = Entities(); 
		
		Entity updated = workflows.Update(new Entity(uuid, name, description, created));
		Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);
    	
    	workflows.Save();

    	if (workflow != null) folder.copy(workflow, "workflow.json");
    	
    	if (data != null) {
        	Folder data_folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid, "data");
        	        	
        	data_folder.delete();
        	
        	data_folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid, "data");

    		for (MultipartFile d: data) data_folder.copy(d);
    	} 
    	
    	return updated;
    }
	
    public void Delete(String uuid) throws Exception {
		Entities<Entity> workflows = Entities(); 
    	Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);

    	folder.delete();
    	workflows.Remove(uuid);
    	workflows.Save();
    }

	public List<File> Execute(Folder scratch, String uuid, JsonNode params) throws Exception {
		PythonProcess p = new PythonProcess(PYTHON_POSTGRES);

		Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);

		// TODO: Just so I don't forget, these are the function calls to publish to GeoServer.
		// TODO: Code doesn't belong here though.
		// GeoServer.post_workspace(schema);
		// GeoServer.post_data_store(schema);
		// GeoServer.post_feature_type(schema, "oil_rings");
		// GeoServer.post_feature_type(schema, "grid_cells");
		// GeoServer.delete_workspace(schema);

		return p.execute(scratch, folder, params);
	}

	public List<File> Execute(Folder scratch, String uuid, String s_params) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode params = mapper.readTree(s_params);

		return this.Execute(scratch, uuid, params);
	}

	public ZipFile ExecuteZip(String uuid, String params) throws Exception {
		UuidFolder scratch = new UuidFolder(APP_FOLDERS_SCRATCH);
		List<File> files = this.Execute(scratch, uuid, params);
		ZipFile zf = new ZipFile(files);

		scratch.delete();

		return zf;
	}
}
