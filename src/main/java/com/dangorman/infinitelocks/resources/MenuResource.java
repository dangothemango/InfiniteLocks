package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.api.Constants;
import com.dangorman.infinitelocks.core.User;
import com.dangorman.infinitelocks.core.Utilities;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/")
public class MenuResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMenu(@CookieParam(Constants.USER_COOKIE) String username, @CookieParam(Constants.SESSION_COOKIE) String sessionId){
        boolean isSessionActive = Utilities.isSessionActive(username,sessionId);
        if (!isSessionActive) {
            return Utilities.getRedirect("/login");
        }
        return renderMenu(username);
    }

    private static String renderMenu(String username) {
        List<String> locks = new User(username).getAvailableLocks();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Menu.html");

        return template.render(new JtwigModel()
                .with("availableLocks",locks));
    }

}
