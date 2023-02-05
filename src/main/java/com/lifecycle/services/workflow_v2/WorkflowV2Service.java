package com.lifecycle.services.workflow_v2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.components.io.Folder;
import com.lifecycle.components.io.UuidFolder;
import com.lifecycle.components.io.ZipFile;
import com.lifecycle.entities.metadata.Entities;
import com.lifecycle.entities.metadata.Entity;
import com.lifecycle.entities.workflow.Workflow;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Date;

@Service
public class WorkflowV2Service {

	@Value("${app.folders.workflows}")
	private String APP_FOLDERS_WORKFLOWS;
	
	@Value("${app.folders.scratch}")
	private String APP_FOLDERS_SCRATCH;

	@Value("${app.list.workflows}")
	private String APP_WORKFLOWS;

	@Value("${app.connection_string}")
	private String DB_CONNECTION_STRING;

    @Autowired
	public WorkflowV2Service() { }
	
    public Entities<Entity> Entities() throws IOException {
    	return new Entities<>(APP_WORKFLOWS, Entity.class);
    }
	
    public File List() throws IOException {
    	return new File(APP_WORKFLOWS);
    }
        
    public Entity Create(String name, String description, MultipartFile workflow) throws Exception {
    	Entities<Entity> workflows = new Entities<>(APP_WORKFLOWS, Entity.class);
		UuidFolder scratch = new UuidFolder(APP_FOLDERS_WORKFLOWS);
		Entity entity = workflows.Add(new Entity(scratch.uuid, name, description));
		
		workflows.Save();
		scratch.copy(workflow, "workflow.json");

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

    public Entity Update(String uuid, String name, String description, Date created, MultipartFile workflow) throws Exception {
		Entities<Entity> workflows = Entities(); 
		
		Entity updated = workflows.Update(new Entity(uuid, name, description, created));
		Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);
    	
    	workflows.Save();

    	if (workflow != null) folder.copy(workflow, "workflow.json");

    	return updated;
    }
	
    public void Delete(String uuid) throws Exception {
		Entities<Entity> workflows = Entities(); 
    	Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);

    	folder.delete();
    	workflows.Remove(uuid);
    	workflows.Save();
    }

	public void Execute(String uuid, String s_params) throws Exception {
		ObjectMapper om = new ObjectMapper();

		// Read in parameters provided by user through web service call
		TypeReference<Map<String, Object>> tr = new TypeReference<>() { };
		Map<String, Object> params = om.readValue(s_params, tr);

		// Read in workflow JSON
		Folder folder = new Folder(APP_FOLDERS_WORKFLOWS, uuid);
		String content = Files.readString(folder.path("workflow.json"));

		// Replace workflow placeholders by user parameters
		StringSubstitutor sub = new StringSubstitutor(params);
		String replaced = sub.replace(content);

		Workflow wf = om.readValue(replaced, Workflow.class);
		Connection conn = DriverManager.getConnection(DB_CONNECTION_STRING);
		wf.execute(conn);
		conn.close();
	}
}
