package com.lifecycle.components.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lifecycle.components.serialization.repeatable.Deserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message_Type {
	private long identifier;
	
	@JsonDeserialize(using = Deserializer.class, contentAs = Field.class)
	private List<Field> field;

	//region getters and setters...
	public long getIdentifier() { return identifier; }

	public void setIdentifier(long identifier) { this.identifier = identifier; }

	public List<Field> getField() { return field; }

	public void setField(List<Field> field) { this.field = field; }
	//endregion

	public Message_Type() { }
}
