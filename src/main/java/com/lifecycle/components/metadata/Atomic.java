package com.lifecycle.components.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Atomic extends Model {
    private String time;
    private State state;

    //region getters and setters...
    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public State getState() { return state; }

    public void setState(State state) { this.state = state; }
    //endregion

    public Atomic() { }
}
