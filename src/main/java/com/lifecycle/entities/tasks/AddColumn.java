package com.lifecycle.entities.tasks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lifecycle.entities.workflow.Task;
import com.lifecycle.utils.Queries;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddColumn extends Task {

    private static class Params {
        public String table;
        public String name;
        public String type;
    }

    private Params params;

    public AddColumn() { }

    @Override
    public void validate() throws Exception {
        // TODO: Other validation, empty, null, etc.
        // TODO: check type is a PostGres data type
        if (output == null) throw new Exception("Output was not provided.");
    }

    public void replace_results(List<Task> tasks) throws Exception {
        params.table = this.get_step_replacement(tasks, params.table);
    }

    @Override
    public void execute(Connection conn) throws Exception {
        Statement st = conn.createStatement();
        st.addBatch(Queries.copy_table(output, params.table));
        st.addBatch(Queries.add_column(output, params.name, params.type));
        st.executeBatch();
        st.close();
    }
}
