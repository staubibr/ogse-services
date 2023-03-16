package com.ogse.components.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupling {
	private String from_model;
	private String from_port;
	private String to_model;
	private String to_port;

	//region getters and setters...
	public String getFrom_model() { return from_model; }

	public void setFrom_model(String from_model) { this.from_model = from_model; }

	public String getFrom_port() { return from_port; }

	public void setFrom_port(String from_port) { this.from_port = from_port; }

	public String getTo_model() { return to_model; }

	public void setTo_model(String to_model) { this.to_model = to_model; }

	public String getTo_port() { return to_port; }

	public void setTo_port(String to_port) { this.to_port = to_port; }
	//endregion
}
