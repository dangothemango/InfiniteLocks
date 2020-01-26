package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.api.Constants;
import com.dangorman.infinitelocks.core.Utilities;
import com.dangorman.infinitelocks.db.DatabaseModule;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/login")
public class LoginResource {

    public static final String GET_PASSWORD_SQL = "Select username,password from users where username = ? limit 1";

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getLogin(@CookieParam(Constants.USER_COOKIE) String username, @CookieParam(Constants.SESSION_COOKIE) String sessionId){
        String userLoggedIn = Utilities.checkLoginStatus(username,sessionId);

        if (userLoggedIn == null) {
            return MenuResource.renderMenu(username);
        } else {
            JtwigTemplate template;
            template = JtwigTemplate.classpathTemplate("assets/Login.html");
            return template.render(new JtwigModel());
        }

    }

    @POST
    public Response attemptLogin(@QueryParam("username") String username, @QueryParam("password") String password) {
        String correctPass;
        Response.ResponseBuilder rb;
        try {
            correctPass = (String)
                    DatabaseModule.rows(GET_PASSWORD_SQL,username)
                    .get(0).get("password");
            if (password.equals(correctPass)) {
                Utilities.deleteExpiredSessions(username);
                System.out.println("Password correct, logging in: "+username);
                String sessionId = UUID.randomUUID().toString();
                Utilities.extendSession(username, sessionId);
                rb = Response.ok("Successful login");
                rb.cookie(Utilities.createSecureCookie(Constants.USER_COOKIE, username),
                        Utilities.createSecureCookie(Constants.SESSION_COOKIE, sessionId));
                return rb.build();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.out.println(String.format("User: %s not found", username));
        }
        rb = Response.status(Response.Status.FORBIDDEN);
        rb.entity("Invalid Username or Password");

        return rb.build();
    }
}
