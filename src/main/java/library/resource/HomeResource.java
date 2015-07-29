package library.resource;

import library.views.HomeView;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by vaibhav.tomar on 22/07/15.
 */
@Path("/home")

public class HomeResource {

    public HomeResource()
    {

    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public HomeView showhome()
    {
        return new HomeView();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String checking()
    {
        return "i got it";
    }

}
