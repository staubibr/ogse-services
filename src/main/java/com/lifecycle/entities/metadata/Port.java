package com.lifecycle.entities.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Port {
	private String type;
	private String name;
	private long message_type;

	// region <getters and setters>
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getMessage_type() {
		return message_type;
	}

	public void setMessage_type(long message_type) {
		this.message_type = message_type;
	}
	// endregion

	public Port() { }
}
