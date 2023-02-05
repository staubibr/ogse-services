package com.lifecycle.entities.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lifecycle.entities.metadata.Coupled;

import java.util.List;

public class Scenario {
    private Coupled metadata;
    private List<Instance> instances;

    @JsonProperty("metadata")
    public Coupled getMetadata() { return this.metadata; }

    @JsonProperty("instances")
    public List<Instance> getInstances() { return this.instances; }
}
