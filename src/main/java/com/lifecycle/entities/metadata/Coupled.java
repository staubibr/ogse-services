package com.lifecycle.entities.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lifecycle.components.serialization.repeatable.Deserializer;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coupled extends Model{
    @JsonDeserialize(using = Deserializer.class, contentAs = Subcomponent.class)
    private List<Subcomponent> subcomponent;

    @JsonDeserialize(using = Deserializer.class, contentAs = Coupling.class)
    private List<Coupling> coupling;

    //region getters and setters...
    public List<Subcomponent> getSubcomponent() { return subcomponent; }

    public void setSubcomponent(List<Subcomponent> subcomponent) { this.subcomponent = subcomponent; }

    public List<Coupling> getCoupling() { return coupling; }

    public void setCoupling(List<Coupling> coupling) { this.coupling = coupling; }
    //endregion
}
