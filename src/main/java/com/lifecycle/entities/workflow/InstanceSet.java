package com.lifecycle.entities.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class InstanceSet {
	private String table;
	private String model_type;
	private String title;
	private String[] properties;

	public InstanceSet() {}

	public String getTable() {
		return this.table;
	}

	public void setTable(String value) { this.table = value; }

	@JsonProperty("model_type")
	public String getModelType() { return this.model_type; }

	@JsonProperty("model_type")
	public void setModelType(String value) { this.model_type = value; }

	@JsonProperty("title")
	public String getTitle() { return this.title; }

	@JsonProperty("title")
	public void setTitle(String value) { this.title = value; }

	public String[] getProperties() { return this.properties; }

	public void setProperties(String[] value) { this.properties = value; }
}
