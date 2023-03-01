package com.lifecycle.services.model;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lifecycle.components.metadata.Inventory;
import com.lifecycle.components.metadata.Entity;
import com.lifecycle.components.io.Folder;

@Service
public class ModelService {

	public final Inventory inventory;
	public final Folder folder;

    @Autowired
	public ModelService(@Value("${app.list.models}") String inventory,
						@Value("${app.folders.models}") String folder) throws IOException {
		// TODO: This may be a mistake. It's constructed only when launching the service. So it desyncs if manual
		// TODO: changes are made to the files (which shouldn't happen). But it could also be a problem if the services
		// TODO: fail once every while and the inventory is not cleaned up.
		this.inventory = new Inventory(inventory);
		this.folder = new Folder(folder);
	}
    
    public Entity Create(String name, String description, MultipartFile model) throws Exception {
		UUID uuid = this.folder.get_uuid();
		this.folder.create(uuid.toString()).copy(model, "model.json");

		Entity entity = this.inventory.Add(new Entity(uuid, name, description));
		this.inventory.Save();

		return entity;
    }

    public Entity Read(String uuid) throws Exception {
		return this.inventory.Get(uuid);
    }
    
    public File ReadFile(String uuid) throws Exception {
    	return this.folder.file(uuid, "model.json");
    }

	public Entity Update(String uuid, String name, String description, Date created, MultipartFile model) throws Exception {
		Entity updated = this.inventory.Update(new Entity(uuid, name, description, created));
		this.inventory.Save();
	
		if (model != null) this.folder.folder(uuid).copy(model, "model.json");
		
		return updated;
	}
	
    public void Delete(String uuid) throws Exception {
    	this.folder.delete(uuid);
		this.inventory.Remove(uuid).Save();
    }
}