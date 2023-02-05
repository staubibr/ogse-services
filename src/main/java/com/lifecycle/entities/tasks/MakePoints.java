package com.lifecycle.entities.tasks;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.lifecycle.entities.workflow.Task;
import com.lifecycle.utils.Queries;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MakePoints extends Task {

    private static class Params {
        public List<List<Float>> points;
    }

    private Params params;

    public MakePoints() { }

    @Override
    public void validate() throws Exception {
        // TODO: Other validation, empty, null, etc.
        for (List<Float> p : this.params.points) {
            if (p.get(0) > 180) throw new Exception("Longitude is higher than 180 degrees.");

            if (p.get(0) < -180) throw new Exception("Longitude is less than -180 degrees.");

            if (p.get(1) > 90) throw new Exception("Latitude is higher than 180 degrees.");

            if (p.get(1) < -90) throw new Exception("Latitude is higher than 180 degrees.");
        }

        if (output == null) throw new Exception("Output was not provided.");
    }

    @Override
    public void execute(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        st.addBatch(Queries.create_point_table(output));
        st.addBatch(Queries.insert_points(output, params.points));
        st.executeBatch();
        st.close();
    }
}
