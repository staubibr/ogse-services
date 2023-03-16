package com.ogse.components.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemporalCoverage {
	private String start;
	private String end;
	private String scheme;

	//region getters and setters...
	public String getStart() { return start; }

	public void setStart(String start) { this.start = start; }

	public String getEnd() { return end; }

	public void setEnd(String end) { this.end = end; }

	public String getScheme() { return scheme; }

	public void setScheme(String scheme) { this.scheme = scheme; }
	//endregion

	public TemporalCoverage() { }
}
