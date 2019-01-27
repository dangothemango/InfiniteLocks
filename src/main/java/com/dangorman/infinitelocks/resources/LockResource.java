package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.db.DatabaseModule;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import com.google.gson.JsonObject;


@Path("/lock")
public class LockResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getLock(@QueryParam("lock") String lock) {
        String lockLocation;
        if (lock.contains(";")){
            throw new HTTPException(401);
        }
        try {
            lockLocation = (String)DatabaseModule.getDbConnection().rows(String.format("Select location from locks where name = '%s' limit 1",lock)).get(0).get("location");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //TODO make all 404s better ---idea Secret locks
            throw new HTTPException(404);
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate(lockLocation);

        return template.render(new JtwigModel());
    }

    @Path("/unlock")
    @Produces(MediaType.APPLICATION_JSON)
    public String unlock(@QueryParam("lock") String lock, @QueryParam("key") String key){
        String allSollutions;
        JsonObject json = new JsonObject();
        json.addProperty( "result","failure");
        try{
            allSollutions = (String)DatabaseModule.getDbConnection().rows(
                    String.format("Select location from locks where name = '%s' limit 1",lock)
                        ).get(0).get("solutions");
            String[] solutionsList = allSollutions.split(",");
            for (String s: solutionsList) {
                if (s.toUpperCase() == key.trim().toUpperCase()){
                    //TODO: do all the usery things
                    json.addProperty("result","success");
                }
            }
        } catch (SQLException e){
        }
        return json.toString();
    }

}
