package com.lifecycle.services.visualization;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lifecycle.components.metadata.Inventory;
import com.lifecycle.components.metadata.Entity;
import com.lifecycle.components.io.Folder;

@Service
public class VisualizationService {

	public final Inventory inventory;
	public final Folder folder;

    @Autowired
	public VisualizationService(@Value("${app.list.visualizations}") String inventory,
								@Value("${app.folders.visualizations}") String folder) throws IOException {
		this.inventory = new Inventory(inventory);
		this.folder = new Folder(folder);
	}

    public Entity Create(String name, String description, Map<String, InputStream> files) throws Exception {
		UUID uuid = this.folder.get_uuid();
		Folder scratch = this.folder.create(uuid.toString());

		for (String f : files.keySet()) scratch.copy(files.get(f), f);

		Entity entity = this.inventory.Add(new Entity(uuid, name, description));
		this.inventory.Save();

		return entity;
    }

	public Entity Create(String name, String description, List<MultipartFile> files) throws Exception {
		Map<String, InputStream> f_map = new HashMap<>();

		for (MultipartFile f: files) f_map.put(f.getOriginalFilename(), f.getInputStream());

		return this.Create(name, description, f_map);
	}

    public Entity Create(String name, String description, MultipartFile visualization, List<File> files) throws Exception {
		Map<String, InputStream> f_map = new HashMap<>();

		for (File f: files) f_map.put(f.getName(), new FileInputStream(f));

		f_map.put(visualization.getOriginalFilename(), visualization.getInputStream());

		return this.Create(name, description, f_map);
    }

    public Entity Read(String uuid) throws Exception {
		return inventory.Get(uuid);
    }

    public byte[] ReadFiles(String uuid) throws Exception {
    	return this.folder.folder(uuid).zip();
    }
    
    public Entity Update(String uuid, String name, String description, Date created, List<MultipartFile> files) throws Exception {
		Entity updated = this.inventory.Update(new Entity(uuid, name, description, created));
		this.inventory.Save();
		this.folder.empty(uuid);

		for (MultipartFile f: files) this.folder.folder(uuid).copy(f, f.getOriginalFilename());

    	return updated;
    }
	
    public void Delete(String uuid) throws Exception {
		this.folder.delete(uuid);
		this.inventory.Remove(uuid).Save();
    }
}
