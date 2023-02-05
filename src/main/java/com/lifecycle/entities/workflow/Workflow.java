package com.lifecycle.entities.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lifecycle.components.TaskDeserializer;
import com.lifecycle.utils.GeoServer;
import com.lifecycle.utils.Queries;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Workflow {
	public HashMap<String, String> input;
	public List<InstanceSet> instances;
	public List<RelationSet> relations;

	@JsonIgnore
	public String schema = null;

	@JsonDeserialize(contentUsing=TaskDeserializer.class)
	public List<Task> tasks;

	public Workflow() {	}

	public void create_schema(Connection conn) throws SQLException {
		schema = "ogse_" + UUID.randomUUID().toString().replaceAll("-", "");

		Statement st = conn.createStatement();
		st.addBatch(Queries.create_schema(schema));
		st.executeBatch();
		st.close();
	}

	public void execute(Connection conn) throws Exception {
		this.create_schema(conn);

		try {
			for (Task t: tasks) {
				if (t.output == null) t.output = "Temp_" + (tasks.indexOf(t) + 1);

				t.replace_results(tasks);
				t.apply_schema(schema);
				t.validate();
				t.execute(conn);
			}
		}
		catch(Exception ex) {
			this.cancel(conn);
			throw ex;
		}



		this.clean_up(conn);

		GeoServer.post_workspace(schema);
		GeoServer.post_data_store(schema);
		GeoServer.post_feature_type(schema, "oil_rings");
		GeoServer.post_feature_type(schema, "grid_cells");
		GeoServer.delete_workspace(schema);
	}

	private void cancel(Connection conn) throws SQLException {
		Statement st = conn.createStatement();
		st.addBatch(Queries.drop_schema(schema));
		st.executeBatch();
		st.close();
	}

	private void clean_up(Connection conn) throws SQLException {
		Statement st = conn.createStatement();

		for (Task t: tasks) if (t.isTemp) st.addBatch(Queries.drop_table(t.output));

		st.executeBatch();
		st.close();
	}
}
