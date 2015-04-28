package flipkart.pricing.apps.kaizen.resources;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @unsderstands TestResourceCreatedToCheckBootstrap
 *
 */
//TODO : Delete this
@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class TestResource {

    @GET
    @Path("/foo")
    public String getFoo() {
        return "fooBarBaaz";
    }
}
