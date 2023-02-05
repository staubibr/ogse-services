package com.lifecycle.entities.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Subcomponent {
	private String identifier;
	private String model;

	//region getters and setters...
	public String getIdentifier() { return identifier; }

	public void setIdentifier(String identifier) { this.identifier = identifier; }

	public String getModel() { return model; }

	public void setModel(String model) { this.model = model; }
	//endregion

	public Subcomponent() { }

	public Subcomponent(String identifier, String model) {
		this.identifier = identifier;
		this.model = model;
	}
}
