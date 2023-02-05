package com.lifecycle.components.serialization.repeatable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Deserializer extends JsonDeserializer<List<Object>> implements ContextualDeserializer {

    private Class<?> contentAs;
    
	@Override
	public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) {

        JsonDeserialize jsonDeserialize = property.getAnnotation(JsonDeserialize.class);
        
        this.contentAs = jsonDeserialize.contentAs();
                
        return this;
	}

	@Override
	public List<Object> deserialize(JsonParser p, DeserializationContext context)
			throws IOException {
	    ObjectCodec oc = p.getCodec();
		JsonNode node = oc.readTree(p);
		List<Object> out = new ArrayList<>();

		if (!node.isArray()) {
			out.add(oc.treeToValue(node, this.contentAs));
		}
		
		else for (JsonNode el: node) {
			out.add(oc.treeToValue(el, this.contentAs));
		}
      
        return out;
	}
}