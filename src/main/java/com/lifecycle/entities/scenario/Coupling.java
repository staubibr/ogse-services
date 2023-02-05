package com.lifecycle.entities.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifecycle.entities.metadata.Atomic;

public class Coupling {
    private String to_port;
    private Instance to_model;
    private String from_port;

    @JsonProperty("to_port")
    public String getInputPort() { return this.to_port; }

    @JsonProperty("to_model")
    public int getOutputModelId() { return this.to_model.getId(); }

    @JsonProperty("to_model_type")
    public String getOutputModel() { return this.to_model.getModelType(); }

    @JsonProperty("from_port")
    public String getFromPort() { return this.from_port; }
}
