package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.api.Constants;
import com.dangorman.infinitelocks.api.UnlockAttempt;
import com.dangorman.infinitelocks.core.Utilities;
import com.dangorman.infinitelocks.db.DatabaseModule;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.http.HTTPException;

import com.google.gson.JsonObject;


@Path("/lock")
public class LockResource {

    private static final String GET_LOCK_SOLUTIONS = "Select solutions from locks where name = ? limit 1";
    private static final String GET_LOCK_SQL = "Select puzzle_html from locks where name = ? limit 1";
    private static final String SOLVED_INSERT_SQL = "insert into solved_puzzles values ('%1$s', '%2$s') on conflict do nothing";


    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getLock(@CookieParam(Constants.USER_COOKIE) String username, @CookieParam(Constants.SESSION_COOKIE) String sessionId,
                          @QueryParam("lock") String lock) {
        String userLoggedIn = Utilities.checkLoginStatus(username,sessionId);
        if (userLoggedIn != null) {
            return userLoggedIn;
        }
        String lockHtml;
        if (lock.contains(";")){
            throw new HTTPException(401);
        }
        try {
            lockHtml = (String)DatabaseModule.rows(GET_LOCK_SQL,lock)
                    .get(0).get("puzzle_html");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO make all 404s better --idea Secret locks
            throw new HTTPException(404);
        }

        JtwigTemplate template = JtwigTemplate.inlineTemplate(lockHtml);

        return template.render(new JtwigModel().with("lockName",lock));
    }

    @Path("/unlock")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String unlock(UnlockAttempt unlockAttempt, @CookieParam(Constants.USER_COOKIE) String username){
        if (unlockAttempt.getLock().contains(";")){
            throw new HTTPException(401);
        }
        String allSolutions;
        String result = "failure";
        System.out.println(unlockAttempt.getLock()+':'+unlockAttempt.getKey());
        try{
            allSolutions = (String)
                    DatabaseModule.rows(GET_LOCK_SOLUTIONS,unlockAttempt.getLock())
                            .get(0).get("solutions");
            System.out.println(allSolutions);
            String[] solutionsList = allSolutions.split(",");
            for (String s: solutionsList) {
                if (s.toUpperCase().equals(unlockAttempt.getKey().trim().toUpperCase())){
                    //TODO: do all the user things
                    result = "success";
                }
            }
        } catch (Exception e){
        }
        JsonObject json = new JsonObject();
        json.addProperty( "result",result);
        return json.toString();
    }

}
