package com.lifecycle.services.complete;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompleteInput {

	UUID workflow_uuid;
	JsonNode workflow_params;
	Long simulation_iterations;
	Double simulation_duration;
	String visualization_name;
	String visualization_description;
}
