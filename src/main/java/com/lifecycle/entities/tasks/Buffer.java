package com.lifecycle.entities.tasks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lifecycle.components.TaskFactory;
import com.lifecycle.entities.workflow.Task;
import com.lifecycle.utils.Queries;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Buffer extends Task {

    private static class Params {
        public float radius;
        public String source;
    }

    private Params params;

    public Buffer() { }

    @Override
    public void validate() throws Exception {
        // TODO: Other validation, empty, null, etc.
        if (this.params.radius <= 0) throw new Exception("Radius is 0 or less.");

        if (output == null) throw new Exception("Output was not provided.");
    }

    public void replace_results(List<Task> tasks) throws Exception {
        params.source = this.get_step_replacement(tasks, params.source);
    }

    @Override
    public void execute(Connection conn) throws Exception {
        Statement st = conn.createStatement();
        st.addBatch(Queries.buffer(output, params.source, params.radius));
        st.addBatch(Queries.drop_column(output, "geom"));
        st.addBatch(Queries.rename_column(output, "buff_geom", "geom"));
        st.executeBatch();
        st.close();
    }
}
