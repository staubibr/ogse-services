package com.lifecycle.components.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class State {
	private String description;
	private long message_type;

	//region getters and setters...
	public String getDescription() { return description; }

	public void setDescription(String description) { this.description = description; }

	public long getMessage_type() { return message_type; }

	public void setMessage_type(long message_type) { this.message_type = message_type; }
	//endregion

	public State() { }
}
