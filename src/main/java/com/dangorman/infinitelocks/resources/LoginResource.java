package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.api.Constants;
import com.dangorman.infinitelocks.core.Utilities;
import com.dangorman.infinitelocks.db.DatabaseModule;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Path("/login")
public class LoginResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getLogin(){
        //TODO: check if user already logged in

        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Login.html");

        return template.render(new JtwigModel());
    }

    @POST
    public Response attemptLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
        String correctPass;
        Response.ResponseBuilder rb;
        try {
            correctPass = (String)
                    DatabaseModule.rows("Select username,password from users where username = ? limit 1",username)
                    .get(0).get("password");
            if (password.equals(correctPass)) {
                Utilities.deleteExpiredSessions(username);
                System.out.println("Password correct, logging in: "+username);
                String sessionId = UUID.randomUUID().toString();
                Date expiry = new Date();
                expiry = Utilities.addDate(expiry, Calendar.DATE, 1);
                DatabaseModule.execute("insert into sessions values (?,?,?)", sessionId, username, expiry);
                rb = Response.ok("Successful login");
                rb.cookie(new NewCookie(Constants.USER_COOKIE, username),
                        new NewCookie(Constants.SESSION_COOKIE, sessionId));

                return rb.build();
            }
        } catch (Exception e) {
            System.out.println(String.format("User: %s not found", username));
        }
        rb = Response.status(Response.Status.FORBIDDEN);
        rb.entity("Invalid Username or Password");

        return rb.build();
    }
}
