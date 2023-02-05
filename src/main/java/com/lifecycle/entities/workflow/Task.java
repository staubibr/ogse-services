package com.lifecycle.entities.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Connection;
import java.util.List;

public abstract class Task {
    public String name;
    public String output = null;

    @JsonIgnore
    public Boolean isTemp = false;

    public Task() { }

    public abstract void execute(Connection conn) throws Exception;

    public abstract void validate() throws Exception;

    public void replace_results(List<Task> tasks) throws Exception { };

    public String get_step_replacement(List<Task> tasks, String value) throws Exception {
        if (!value.startsWith("@step")) return value;

        int i = Integer.parseInt(value.substring(6)) - 1;

        if (i >= tasks.indexOf(this)) throw new Exception("Cannot use results from a task not yet executed.");

        return tasks.get(i).output;
    }

    public void apply_schema(String schema) {
        output = schema + "." + output;
    }
}
