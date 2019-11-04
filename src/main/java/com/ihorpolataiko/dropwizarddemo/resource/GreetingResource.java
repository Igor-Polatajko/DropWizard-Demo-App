package com.ihorpolataiko.dropwizarddemo.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/greeting")
@Produces(MediaType.APPLICATION_JSON)
public class GreetingResource {

    @GET
    public String defaultHello() {
        return "Hello world!";
    }

    @GET
    @Path("/{name}")
    public String namedHello(@PathParam("name") String name) {
        return "Hello, " + name + "!";
    }
}
