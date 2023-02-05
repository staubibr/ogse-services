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
public class Intersection extends Task {

    private static class Params {
        public String table1;
        public String table2;
    }

    private Params params;

    public Intersection() { }

    @Override
    public void validate() throws Exception {
        if (params.table1 == null) throw new Exception("Table 1 not provided.");

        if (params.table2 == null) throw new Exception("Table 2 not provided.");

        if (output == null) throw new Exception("Output was not provided.");
    }

    public void replace_results(List<Task> tasks) throws Exception {
        params.table1 = this.get_step_replacement(tasks, params.table1);
        params.table2 = this.get_step_replacement(tasks, params.table2);
    }

    @Override
    public void execute(Connection conn) throws Exception {
        Statement st = conn.createStatement();
        st.addBatch(Queries.intersect(output, params.table1, params.table2));
        st.executeBatch();
        st.close();
    }
}
