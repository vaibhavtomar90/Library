package library.resource;

import library.DAO.AuthorDAO;
import library.Representations.Author;
import library.views.AuthorView;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by vaibhav.tomar on 27/07/15.
 */

@Path("/authors")
public class AuthorResource {

    private AuthorDAO authorDAO;
    public AuthorResource()
    {
        authorDAO=null;
    }
    public AuthorResource(DBI jdbi)
    {
        authorDAO=jdbi.onDemand(AuthorDAO.class);
    }
    @GET
    @Produces("text/html")
    public AuthorView authorsHome()
    {
        return new AuthorView();
    }
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response showAuthors()
    {
        List<Author> authors=authorDAO.showAuthors();
        return Response.ok(authors).build();
    }
    @POST
    @Path("/create")
    public void createAuthor(@FormParam("authorid") int authorid,
                             @FormParam("firstname") String firstname,
                             @FormParam("lastname") String lastname)
    {
        authorDAO.createAuthor(authorid,firstname,lastname);

    }
    @POST
    @Path("/update")
    public void updateAuthor(@FormParam("authorid") int authorid,
                             @FormParam("firstname") String firstname,
                             @FormParam("lastname") String lastname)
    {

        authorDAO.updateAuthor(authorid, firstname, lastname);
    }
    @POST
    @Path("/delete")
    public void deleteAuthor(@FormParam("authorid") int authorid)
    {
        authorDAO.deleteAuthor(authorid);
    }
}
