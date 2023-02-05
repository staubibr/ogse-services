package com.lifecycle.entities.tasks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lifecycle.entities.workflow.Task;
import com.lifecycle.utils.Queries;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Rings extends Task {

    private static class Params {
        public List<Float> radii;
        public String source;
    }

    private Params params;

    public Rings() { }

    @Override
    public void validate() throws Exception {
        // TODO: Other validation, empty, null, etc.
        if (this.params.radii.size() == 0) throw new Exception("No radius provided.");

        // TODO: Check radii ascending order

        if (output == null) throw new Exception("Output was not provided.");
    }

    public void replace_results(List<Task> tasks) throws Exception {
        params.source = this.get_step_replacement(tasks, params.source);
    }

    @Override
    public void execute(Connection conn) throws Exception {
        Statement st = conn.createStatement();

        st.addBatch(Queries.create_ring(output, params.source, params.radii.get(0), params.radii.get(1)));
        st.addBatch(String.format("ALTER TABLE %s ADD COLUMN ring_id SERIAL PRIMARY KEY;", output));

        for (int i = 2; i < params.radii.size(); i++) {
            st.addBatch(Queries.insert_ring(output, params.source, params.radii.get(i - 1), params.radii.get(i)));
        }

        st.addBatch(Queries.drop_column(output, "geom"));
        st.addBatch(Queries.rename_column(output, "ring_geom", "geom"));
        st.addBatch(Queries.drop_column(output, "id"));
        st.addBatch(Queries.rename_column(output, "ring_id", "id"));
        st.executeBatch();
        st.close();
    }
}
