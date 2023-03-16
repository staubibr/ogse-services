package com.ogse.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GeoServer {
    public static int post(String url, String body) throws Exception {
        String auth = "Basic " + Base64.getEncoder().encodeToString("admin:geoserver".getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> r = client.send(request, HttpResponse.BodyHandlers.ofString());

        return r.statusCode();
    }

    public static int delete(String url) throws Exception {
        String auth = "Basic " + Base64.getEncoder().encodeToString("admin:geoserver".getBytes());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", auth)
                .DELETE()
                .build();

        HttpResponse<String> r = client.send(request, HttpResponse.BodyHandlers.ofString());

        return r.statusCode();
    }

    public static void delete_data_store(String schema) throws Exception {
        String url = "http://localhost:8081/geoserver/rest/workspaces/ogse/datastores/" + schema +"?recurse=true";

        int code = delete(url);

        if (code == 200) return;

        throw new Exception(String.format("Unable to delete data store %s (status code %d).", schema, code));
    }

    public static void delete_workspace(String schema) throws Exception {
        String url = "http://localhost:8081/geoserver/rest/workspaces/" + schema +"?recurse=true";

        int code = delete(url);

        if (code == 200) return;

        throw new Exception(String.format("Unable to delete workspace %s (status code %d).", schema, code));
    }

    public static void post_workspace(String schema) throws Exception {
        String url = "http://localhost:8081/geoserver/rest/workspaces";

        int code = post(url, workspace(schema));

        if (code == 201) return;

        throw new Exception(String.format("Unable to create workspace %s (status code %d).", schema, code));
    }

    public static void post_data_store(String schema) throws Exception {
        String url = "http://localhost:8081/geoserver/rest/workspaces/" + schema + "/datastores";

        int code = post(url, data_store(schema));

        if (code == 201) return;

        throw new Exception(String.format("Unable to create data store %s (status code %d).", schema, code));
    }

    public static void post_feature_type(String schema, String name) throws Exception {
        String url = "http://localhost:8081/geoserver/rest/workspaces/" + schema + "/datastores/" + schema + "/featuretypes";

        int code = post(url, feature_type(schema, name));

        if (code == 201) return;

        throw new Exception(String.format("Unable to create feature type %s in store %s (status code %d).", name, schema, code));
    }

    public static String workspace(String schema) {
        return "{" +
                "    'workspace': {" +
                "        'name': '" + schema + "'," +
                "        'isolated': false," +
                "        'dateCreated': '2022-12-18 18:08:40.870 UTC'," +
                "        'dataStores': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/datastores.json'," +
                "        'coverageStores': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/coveragestores.json'," +
                "        'wmsStores': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/wmsstores.json'," +
                "        'wmtsStores': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/wmtsstores.json'" +
                "    }" +
                "}";
    }

    public static String data_store(String schema) {
        return   "{" +
                  "    'dataStore': {" +
                 "        'name': '" + schema + "'," +
                 "        'description': 'Online Geospatial Simulation Environment'," +
                 "        'type': 'PostGIS'," +
                 "        'enabled': true," +
                 "        'workspace': {" +
                 "            'name': '" + schema + "'," +
                 "            'href': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + ".json'" +
                 "        }," +
                 "        'connectionParameters': {" +
                 "            'entry': [{" +
                 "                    '@key': 'schema'," +
                 "                    '$': '" + schema + "'" +
                 "                }, {" +
                 "                    '@key': 'Evictor run periodicity'," +
                 "                    '$': '300'" +
                 "                }, {" +
                 "                    '@key': 'Max open prepared statements'," +
                 "                    '$': '50'" +
                 "                }, {" +
                 "                    '@key': 'encode functions'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'Batch insert size'," +
                 "                    '$': '100'" +
                 "                }, {" +
                 "                    '@key': 'preparedStatements'," +
                 "                    '$': 'false'" +
                 "                }, {" +
                 "                    '@key': 'database'," +
                 "                    '$': 'postgres'" +
                 "                }, {" +
                 "                    '@key': 'host'," +
                 "                    '$': 'localhost'" +
                 "                }, {" +
                 "                    '@key': 'Loose bbox'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'SSL mode'," +
                 "                    '$': 'DISABLE'" +
                 "                }, {" +
                 "                    '@key': 'Estimated extends'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'fetch size'," +
                 "                    '$': '50000'" +
                 "                }, {" +
                 "                    '@key': 'Expose primary keys'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'validate connections'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'Support on the fly geometry simplification'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'Connection timeout'," +
                 "                    '$': '120'" +
                 "                }, {" +
                 "                    '@key': 'create database'," +
                 "                    '$': 'false'" +
                 "                }, {" +
                 "                    '@key': 'Method used to simplify geometries'," +
                 "                    '$': 'FAST'" +
                 "                }, {" +
                 "                    '@key': 'port'," +
                 "                    '$': '5432'" +
                 "                }, {" +
                 "                    '@key': 'passwd'," +
                 "                    '$': 'crypt1:ZwJ96CpE5YaPiUs0GRmm1g=='" +
                 "                }, {" +
                 "                    '@key': 'min connections'," +
                 "                    '$': '1'" +
                 "                }, {" +
                 "                    '@key': 'dbtype'," +
                 "                    '$': 'postgis'" +
                 "                }, {" +
                 "                    '@key': 'namespace'," +
                 "                    '$': '" + schema + "'" +
                 "                }, {" +
                 "                    '@key': 'max connections'," +
                 "                    '$': '10'" +
                 "                }, {" +
                 "                    '@key': 'Evictor tests per run'," +
                 "                    '$': '3'" +
                 "                }, {" +
                 "                    '@key': 'Test while idle'," +
                 "                    '$': 'true'" +
                 "                }, {" +
                 "                    '@key': 'user'," +
                 "                    '$': 'service'" +
                 "                }, {" +
                 "                    '@key': 'Max connection idle time'," +
                 "                    '$': '300'" +
                 "                }" +
                 "            ]" +
                 "        }," +
                 "        '_default': false," +
                 "        'dateCreated': '2022-12-18 18:19:12.478 UTC'," +
                 "        'dateModified': '2022-12-18 18:44:59.512 UTC'," +
                 "        'disableOnConnFailure': false," +
                 "        'featureTypes': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/datastores/" + schema + "/featuretypes.json'" +
                 "    }" +
                 "}";
    }

    public static String feature_type(String schema, String name) {
        return  "{" +
                "    'featureType': {" +
                "        'name': '" + name + "'," +
                "        'nativeName': '" + name + "'," +
                "        'namespace': {" +
                "            'name': '" + schema + "'," +
                "            'href': 'http://localhost:8081/geoserver/rest/namespaces/" + schema + ".json'" +
                "        }," +
                "        'title': '" + name + "'," +
                "        'keywords': {" +
                "            'string': ['features']" +
                "        }," +
                "        'nativeCRS': 'GEOGCS[\"WGS 84\", DATUM[\"World Geodetic System 1984\", SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], AUTHORITY[\"EPSG\",\"6326\"]], PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], UNIT[\"degree\", 0.017453292519943295], AXIS[\"Geodetic longitude\", EAST], AXIS[\"Geodetic latitude\", NORTH], AUTHORITY[\"EPSG\",\"4326\"]]'," +
                "        'srs': 'EPSG:4326'," +
                "        'nativeBoundingBox': {" +
                "            'minx': -180," +
                "            'maxx': 180," +
                "            'miny': -90," +
                "            'maxy': 90," +
                "            'crs': 'EPSG:4326'" +
                "        }," +
                "        'latLonBoundingBox': {" +
                "            'minx': -180," +
                "            'maxx': 180," +
                "            'miny': -90," +
                "            'maxy': 90," +
                "            'crs': 'EPSG:4326'" +
                "        }," +
                "        'projectionPolicy': 'FORCE_DECLARED'," +
                "        'enabled': true," +
                "        'store': {" +
                "            '@class': 'dataStore'," +
                "            'name': 'ogse:" + schema + "'," +
                "            'href': 'http://localhost:8081/geoserver/rest/workspaces/" + schema + "/datastores/" + schema + ".json'" +
                "        }," +
                "        'serviceConfiguration': false," +
                "        'simpleConversionEnabled': false," +
                "        'internationalTitle': ''," +
                "        'internationalAbstract': ''," +
                "        'maxFeatures': 0," +
                "        'numDecimals': 0," +
                "        'padWithZeros': false," +
                "        'forcedDecimal': false," +
                "        'overridingServiceSRS': false," +
                "        'skipNumberMatched': false," +
                "        'circularArcPresent': false," +
                /*
                "        'attributes': {" +
                "            'attribute': [{" +
                "                    'name': 'id_0'," +
                "                    'minOccurs': 1," +
                "                    'maxOccurs': 1," +
                "                    'nillable': false," +
                "                    'binding': 'java.lang.Integer'" +
                "                }, {" +
                "                    'name': 'geom'," +
                "                    'minOccurs': 0," +
                "                    'maxOccurs': 1," +
                "                    'nillable': true," +
                "                    'binding': 'org.locationtech.jts.geom.MultiPolygon'" +
                "                }, {" +
                "                    'name': 'id'," +
                "                    'minOccurs': 0," +
                "                    'maxOccurs': 1," +
                "                    'nillable': true," +
                "                    'binding': 'java.lang.Integer'" +
                "                }, {" +
                "                    'name': 'idnum'," +
                "                    'minOccurs': 0," +
                "                    'maxOccurs': 1," +
                "                    'nillable': true," +
                "                    'binding': 'java.lang.Long'" +
                "                }" +
                "            ]" +
                "        }" +
                */
                "    }" +
                "}";
    }
}
