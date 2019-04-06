package com.dangorman.infinitelocks.resources;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/menu")
public class MenuResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getMenu(){
        //TODO: USERS

        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Menu.html");

        return template.render(new JtwigModel());
    }

}
