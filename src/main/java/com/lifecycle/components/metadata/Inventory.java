package com.lifecycle.components.metadata;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class Inventory {

	public File file;
	public List<Entity> entities;
	private ObjectMapper mapper = new ObjectMapper();

	public Inventory(String s_file) throws IOException {
		this.file = new File(s_file);

		if (!this.file.exists()) throw new IOException("Cannot read entities, file " + s_file + " does not exist.");

		CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, Entity.class);
		
    	this.entities = this.mapper.readValue(this.file, typeReference);    	 	
	} 
		
	public Entity Add(Entity entity) {
		this.entities.add(entity);

    	entity.setCreated(new Date());
    	
		return entity;
	}
	
	public Inventory Remove(Entity entity) {
		this.entities.remove(entity);

		return this;
	}

	public Inventory Remove(String uuid) throws Exception {
		Entity meta = this.Get((e) -> e.getUuid().toString().equals(uuid));
    	    	
    	return this.Remove(meta);
	}

	public Inventory Remove(UUID uuid) throws Exception {
		return this.Remove(uuid.toString());
	}
	
	public Entity Update(Entity curr) throws Exception {
		Entity prev = this.Get(curr::compareUuid);
    	
    	prev.update(curr);

		this.mapper.writeValue(this.file, entities);
    	
    	return prev;
	}
	
	public Inventory Save() throws IOException {
        this.mapper.writeValue(this.file, entities);

		return this;
	}

	public Entity Get(ICompare<Entity> fn) throws Exception {
		return this.entities.stream()
				.filter(fn::IsEqual)
				.findAny()
				.orElseThrow(() -> new Exception("Unable to find entity requested."));
	}
	public Entity Get(String uuid) throws Exception {
		return this.Get((e) -> e.getUuid().toString().equals(uuid));
	}

	public Entity Get(UUID uuid) throws Exception {
		return this.Get(uuid.toString());
	}

	public interface ICompare<T> {
		boolean IsEqual(T entity);
	}
}
