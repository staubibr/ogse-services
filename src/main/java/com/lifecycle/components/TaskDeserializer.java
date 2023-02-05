package com.lifecycle.components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.lifecycle.entities.workflow.Task;

import java.io.IOException;

public class TaskDeserializer extends StdDeserializer<Task> {

    public TaskDeserializer() {
        this(null);
    }

    public TaskDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Task deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get("name").textValue();

        try {
            Task t = TaskFactory.make(name, node);

            t.isTemp = t.output == null;

            return t;
        }
        catch(Exception ex) {
            throw new IOException("Unable to deserialize task " + name + ": " + ex.getMessage().split("\n")[0]);
        }
    }
}