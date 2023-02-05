package com.lifecycle.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifecycle.entities.tasks.*;
import com.lifecycle.entities.workflow.Task;

import java.util.HashMap;

public class TaskFactory {

    static HashMap<String, String> definitions;

    static {
        TaskFactory.definitions = new HashMap<>();

        TaskFactory.register("buffer", Buffer.class.getName());
        TaskFactory.register("make_points", MakePoints.class.getName());
        TaskFactory.register("intersection", Intersection.class.getName());
        TaskFactory.register("project", Project.class.getName());
        TaskFactory.register("rings", Rings.class.getName());
        TaskFactory.register("add_column", AddColumn.class.getName());
        TaskFactory.register("set_values", SetValues.class.getName());
    }

    static public void register(String id, String className) {
        TaskFactory.definitions.put(id, className);
    }

    static Task make(String id, JsonNode node) throws Exception {
        ObjectMapper om = new ObjectMapper();
        Class<?> c;

        try {
            c = Class.forName(TaskFactory.definitions.get(id));
        }
        catch (ClassNotFoundException ex) {
            throw new Exception("Task " + id + " does not exist.");
        }

        return (Task) om.convertValue(node, c);
    }
}
