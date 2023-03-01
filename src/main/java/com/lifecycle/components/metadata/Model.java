package com.lifecycle.components.metadata;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lifecycle.components.serialization.repeatable.Deserializer;
import com.lifecycle.components.serialization.repeatable.Serializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"identifier", "title", "alternative", "creator", "contributor", "type", "language",
		"description", "subject", "spatial_coverage", "temporal_coverage", "license", "created",
		"modified", "time", "behavior", "state", "subcomponent", "coupling", "port", "message"})
public class Model {

	private String identifier;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> title;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> alternative;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> creator;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> contributor;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> language;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> description;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> subject;
	
	@JsonProperty("spatial coverage")
	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = SpatialCoverage.class)
	private List<SpatialCoverage> spatial_coverage;

	@JsonProperty("temporal coverage")
	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = TemporalCoverage.class)
	private List<TemporalCoverage> temporal_coverage;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> license;

	private Date created;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> modified;

	@JsonSerialize(using = Serializer.class)
	@JsonDeserialize(using = Deserializer.class, contentAs = String.class)
	private List<String> behavior;

	@JsonDeserialize(using = Deserializer.class, contentAs = Port.class)
	private List<Port> port;

	@JsonDeserialize(using = Deserializer.class, contentAs = Message_Type.class)
	private List<Message_Type> message_type;

	//region getters and setters...
	public String getIdentifier() { return identifier; }

	public void setIdentifier(String identifier) { this.identifier = identifier; }

	public List<String> getTitle() { return title; }

	public void setTitle(List<String> title) { this.title = title; }

	public List<String> getAlternative() { return alternative; }

	public void setAlternative(List<String> alternative) { this.alternative = alternative; }

	public List<String> getCreator() { return creator; }

	public void setCreator(List<String> creator) { this.creator = creator; }

	public List<String> getContributor() { return contributor; }

	public void setContributor(List<String> contributor) { this.contributor = contributor; }

	public List<String> getLanguage() { return language; }

	public void setLanguage(List<String> language) { this.language = language; }

	public List<String> getDescription() { return description; }

	public void setDescription(List<String> description) { this.description = description; }

	public List<String> getSubject() { return subject; }

	public void setSubject(List<String> subject) { this.subject = subject; }

	public List<SpatialCoverage> getSpatial_coverage() { return spatial_coverage; }

	public void setSpatial_coverage(List<SpatialCoverage> spatial_coverage) { this.spatial_coverage = spatial_coverage; }

	public List<TemporalCoverage> getTemporal_coverage() { return temporal_coverage; }

	public void setTemporal_coverage(List<TemporalCoverage> temporal_coverage) { this.temporal_coverage = temporal_coverage; }

	public List<String> getLicense() { return license; }

	public void setLicense(List<String> license) { this.license = license; }

	public Date getCreated() { return created; }

	public void setCreated(Date created) { this.created = created; }

	public List<String> getModified() { return modified; }

	public void setModified(List<String> modified) { this.modified = modified; }

	public List<String> getBehavior() { return behavior; }

	public void setBehavior(List<String> behavior) { this.behavior = behavior; }

	public List<Port> getPort() { return port; }

	public void setPort(List<Port> port) { this.port = port; }

	public List<Message_Type> getMessage_type() { return message_type; }

	public void setMessage_type(List<Message_Type> message_type) { this.message_type = message_type; }
	//endregion

	public Model() { }
}
