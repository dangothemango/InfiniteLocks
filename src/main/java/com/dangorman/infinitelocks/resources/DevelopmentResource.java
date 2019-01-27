package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.db.DatabaseModule;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.sql.GroovyRowResult;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import java.util.List;

@Path("/dev")
@Produces(MediaType.APPLICATION_JSON)
public class DevelopmentResource {

    public static class sqlQuery {
        @JsonProperty("query")
        public String query;

        @JsonProperty("auth")
        public String auth;
    }

    @POST
    @Path("/sql")
    public String runSql(sqlQuery query){
        if (!query.auth.equals("dangomango")){
            throw new HTTPException(401);
        }
        System.out.println(query.query);
        try {
            System.out.println(DatabaseModule.getDbConnection() == null ? "itsnull": "its not");
            DatabaseModule.getDbConnection().execute(query.query);
            return "successful";
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @POST
    @Path("/query")
    public String runQuery(sqlQuery query){
        if (!query.auth.equals("dangomango")){
            throw new HTTPException(401);
        }
        System.out.println(query.query);
        try {
            System.out.println(DatabaseModule.getDbConnection() == null ? "itsnull": "its not");
            return DatabaseModule.getDbConnection().rows(query.query).toString();
        } catch (SQLException e) {
            return null;
        }
    }

}
