package com.ogse.components.metadata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Entity {

	private UUID uuid;
	private String name;
	private String description;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date created;

	//region getters and setters...
	public UUID getUuid() { return uuid; }

	public void setUuid(UUID uuid) { this.uuid = uuid; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public Date getCreated() { return created; }

	public void setCreated(Date created) { this.created = created; }
	//endregion

	public Entity() { }

	public Entity(UUID uuid, String name, String description, Date created) {
    	this.uuid = uuid;
    	this.name = name;
    	this.description = description;
    	this.created = created;
    }

    public Entity(UUID uuid, String name, String description) {    	
    	this(uuid, name, description, null);
    }

    public Entity(String s_uuid, String name, String description, Date created) {    	
    	this(s_uuid == null ? null: UUID.fromString(s_uuid), name, description, created);
    }
    
    public Boolean compareUuid(Entity m) {
    	return this.getUuid().equals(m.getUuid());
    }
	
    public Entity update(Entity meta) {
    	if (meta.getName() != null) this.setName(meta.getName());
    	if (meta.getDescription() != null) this.setDescription(meta.getDescription());
    	if (meta.getCreated() != null) this.setCreated(meta.getCreated());

		return this;
    }
    
	public ObjectNode json() {
    	final JsonNodeFactory factory = JsonNodeFactory.instance;
        
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    	return factory.objectNode()
					  .put("uuid", this.uuid.toString())
					  .put("name", this.name)
					  .put("description", this.description)
					  .put("created", df.format(this.created));
	}
}
