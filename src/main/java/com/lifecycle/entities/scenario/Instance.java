package com.lifecycle.entities.scenario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.lifecycle.entities.metadata.Atomic;

import java.util.List;

public class Instance {
    private int id;
    private Atomic model_type;
    private JsonNode params;
    private List<Coupling> couplings;

    @JsonProperty("id")
    public int getId() { return this.id; }

    @JsonProperty("model_type_id")
    public String getModelTypeId() { return this.model_type.getIdentifier(); }

    @JsonProperty("model_type")
    public String getModelType() { return this.model_type.getTitle().get(0); }

    @JsonProperty("params")
    public JsonNode getParams() { return this.params; }

    @JsonProperty("couplings")
    public List<Coupling> getCouplings() { return this.couplings; }
}
