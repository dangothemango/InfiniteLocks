package com.dangorman.infinitelocks.resources;

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

@Path("/")
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
            correctPass = (String) DatabaseModule.getDbConnection().rows(
                    String.format("Select username,password from users where username = '%s' limit 1",username)
            ).get(0).get("password");
            if (password.equals(correctPass)) {
                System.out.println("Password correct, logging in: "+username);
                String sessionId = UUID.randomUUID().toString();
                Date expiry = new Date();
                expiry = Utilities.addDate(expiry, Calendar.DATE, 1);
                DatabaseModule.getDbConnection().execute(
                        String.format("insert into sessions values ('%s', '%s','%tD')", sessionId, username, expiry)
                );

                rb = Response.ok("Successful login");
                rb.cookie(new NewCookie("username", username),
                        new NewCookie("sessionId", sessionId));

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