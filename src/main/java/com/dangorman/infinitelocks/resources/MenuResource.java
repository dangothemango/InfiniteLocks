package com.dangorman.infinitelocks.resources;

import com.dangorman.infinitelocks.core.User;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/menu")
public class MenuResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMenu(){
        //TODO: USERS

        List<String> locks = new User().getAvailableLocks();

        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Menu.html");

        return template.render(new JtwigModel()
                .with("availableLocks",locks)
        );
    }

}
