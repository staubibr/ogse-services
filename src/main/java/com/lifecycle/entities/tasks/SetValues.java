package com.lifecycle.entities.tasks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lifecycle.entities.workflow.Task;
import com.lifecycle.utils.Queries;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SetValues extends Task{
    private static class Params {
        public String table;
        public List<List<String>> where;
    }

    private Params params;

    public SetValues() { }

    @Override
    public void validate() throws Exception {
        // TODO: Other validation, empty, null, etc.
        if (params.where.size() == 0) throw new Exception("No where clauses provided.");

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

        for (List<String> w: params.where) st.addBatch(Queries.update_column(output, w.get(2), w.get(3), w.get(0), w.get(1)));

        st.executeBatch();
        st.close();
    }
}
