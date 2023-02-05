package com.lifecycle.utils;

import com.lifecycle.entities.workflow.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Queries {
    public static String create_uuid_schema(Connection conn) throws SQLException {
        String schema = "ogse_" + UUID.randomUUID().toString().replaceAll("-", "");

        Queries.create_schema(conn, schema);

        return schema;
    }

    public static void create_schema(Connection conn, String name) throws SQLException {
        Statement st = conn.createStatement();
        st.addBatch("CREATE SCHEMA " + name);
        st.executeBatch();
        st.close();
    }

    public static String drop_schema(String schema) {
        return "DROP SCHEMA " + schema + " CASCADE";
    }

    public static String copy_table(String table, String source) {
        return "CREATE TABLE " + table + " AS SELECT * FROM " + source;
    }

    public static String add_column(String table, String column, String type) {
        String template = "ALTER TABLE %s ADD COLUMN %s %s";

        return String.format(template, table, column, type);
    }

    public static String drop_table(String table) {
        return "DROP TABLE " + table + " CASCADE";
    }

    public static String create_schema(String schema) {
        return "CREATE SCHEMA " + schema;
    }

    public static String create_point_table(String table) {
        return "CREATE TABLE " + table + " (id serial, geom geometry(Point, 4326))";
    }

    public static String create_polygon_table(String table) {
        return "CREATE TABLE " + table + " (id serial, geom geometry(Polygon, 4326))";
    }

    public static String insert_points(String table, List<List<Float>> points) {
        String template = "(ST_GeomFromText('POINT(%f %f)', 4326))";

        List<String> values = points.stream()
                                    .map(p -> String.format(template, p.get(0), p.get(1)))
                                    .collect(Collectors.toList());

        return "INSERT INTO " + table + "(geom) VALUES " + String.join(",", values);
    }

    public static String rename_column(String table, String old_column, String new_column) {
        String template = "ALTER TABLE %s RENAME COLUMN %s TO %s";

        return String.format(template, table, old_column, new_column);
    }

    public static String drop_column(String table, String column) {
        String template = "ALTER TABLE %s DROP COLUMN %s";

        return String.format(template, table, column);
    }

    public static String project(String table, String source, int srid) {
        String template = "CREATE TABLE %s AS (SELECT a.*, ST_Transform(geom, %d) as proj_geom FROM %s a)";

        return String.format(template, table, srid, source);
    }

    public static String buffer(String table, String source, float radius) {
        String template = "CREATE TABLE %s AS (SELECT a.*, ST_Buffer(geom, %f) as buff_geom FROM %s a)";

        return String.format(template, table, source, radius);
    }

    public static String intersect(String table, String t1, String t2) {
        String template = "CREATE TABLE %s AS (SELECT a.*, b.id as intersect_id FROM %s a, %s b WHERE ST_Intersects(a.geom, b.geom))";

        return String.format(template, table, t1, t2);
    }

    public static String create_ring(String table, String source, Float r0, Float r1) {
        String template = "CREATE TABLE %s AS SELECT a.*, ST_Difference(ST_Buffer(a.geom, %f), ST_Buffer(a.geom, %f)) AS ring_geom FROM %s a";

        return String.format(template, table, r1, r0, source);
    }

    public static String insert_ring(String table, String source, Float r0, Float r1) {
        String template = "INSERT INTO %s SELECT a.*, ST_Difference(ST_Buffer(a.geom, %f), ST_Buffer(a.geom, %f)) AS ring_geom FROM %s a";

        return String.format(template, table, r1, r0, source);
    }

    public static String update_column(String table, String column, String value, String w_column, String w_value) {
        String template = "UPDATE %s SET %s = %s WHERE %s = %s";

        return String.format(template, table, column, value, w_column, w_value);
    }
}
