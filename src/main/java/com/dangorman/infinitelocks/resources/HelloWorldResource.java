package com.dangorman.infinitelocks.resources;

import com.codahale.metrics.annotation.Timed;
import com.dangorman.infinitelocks.api.Saying;
import com.dangorman.infinitelocks.db.DatabaseModule;
import jline.internal.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        String n;
        try{
            n = (String)DatabaseModule.getDbConnection().rows("Select Top 1 name from names where active = 1").get(0).get("name");
        } catch (SQLException e) {
            Log.error(e.getMessage());
            n = e.getMessage();
        } catch (Exception e){
            Log.error(e.getMessage());
            n = e.getMessage();
        }
        final String value = String.format(template, n);
        return new Saying(counter.incrementAndGet(), value);
    }
}
