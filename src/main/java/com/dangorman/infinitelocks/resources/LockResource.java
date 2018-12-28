package com.dangorman.infinitelocks.resources;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.web.servlet.JtwigRenderer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


@Path("/lock")
public class LockResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getLock(@QueryParam("lock") String lock) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("assets/Puzzles/Colors.html");

        return template.render(new JtwigModel());
    }

}
