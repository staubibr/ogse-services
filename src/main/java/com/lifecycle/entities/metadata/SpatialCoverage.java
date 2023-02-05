package com.lifecycle.entities.metadata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lifecycle.components.serialization.repeatable.Deserializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpatialCoverage {
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> placename;
	
	@JsonDeserialize(using = Deserializer.class, contentAs = Extent.class)
	private List<Extent> extent;

	//region getters and setters...
	public List<String> getPlacename() { return placename; }

	public void setPlacename(List<String> placename) { this.placename = placename; }

	public List<Extent> getExtent() { return extent; }

	public void setExtent(List<Extent> extent) { this.extent = extent; }
	//endregion

	public SpatialCoverage() { }
}
