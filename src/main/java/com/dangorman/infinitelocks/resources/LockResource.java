package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.api.UnlockAttempt;
import com.dangorman.infinitelocks.db.DatabaseModule;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.*;
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

        return template.render(new JtwigModel().with("lockName",lock));
    }

    @Path("/unlock")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String unlock(UnlockAttempt unlockAttempt){
        if (unlockAttempt.getLock().contains(";")){
            throw new HTTPException(401);
        }
        String allSollutions;
        String result = "failure";
        System.out.println(unlockAttempt.getLock()+':'+unlockAttempt.getKey());
        try{
            allSollutions = (String)DatabaseModule.getDbConnection().rows(
                    String.format("Select solutions from locks where name = '%s' limit 1",unlockAttempt.getLock())
                        ).get(0).get("solutions");
            System.out.println(allSollutions);
            String[] solutionsList = allSollutions.split(",");
            for (String s: solutionsList) {
                if (s.toUpperCase().equals(unlockAttempt.getKey().trim().toUpperCase())){
                    //TODO: do all the usery things
                    result = "success";
                }
            }
        } catch (SQLException e){
        }
        JsonObject json = new JsonObject();
        json.addProperty( "result",result);
        return json.toString();
    }

}
