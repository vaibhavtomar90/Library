package library.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by vaibhav.tomar on 20/07/15.
 */

@Path("/library")
@Produces(MediaType.TEXT_HTML)
public class LibraryResource {

      public LibraryResource()
      {

      }
    @GET
    public String printit()
    {
        return "<h1>HELLO</h1>";
    }
}
