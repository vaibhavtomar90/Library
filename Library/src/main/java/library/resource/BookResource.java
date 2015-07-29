package library.resource;

import library.DAO.BooksDAO;
import library.Representations.Book;
import library.views.BookView;
import library.views.HomeView;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by vaibhav.tomar on 22/07/15.
 */

@Path("/books")
public class BookResource {

    private final BooksDAO booksDAO;
    DBI dbi;
    Handle h;
    public BookResource(DBI jdbi) {
        booksDAO = jdbi.onDemand(BooksDAO.class);
        dbi=jdbi;
        dbi.open();
    }
    public BookResource()
    {
        booksDAO=null;
    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public BookView showBooks()
    {
        System.out.print("\n\n\n\n\n\ncheck it now\n\n\n\n");


//        List<Map<String ,Object>> rs=h.select("select * from books");
//        Map<String,Object> row =rs.get(0);
//        System.out.print(row.get("isbn"));
        return new BookView();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("text/html")
    public String createBook(Book book) throws URISyntaxException
    {
        System.out.print("\n\n\n\n\n\n\n\n\n\nhelloabcdefgh\n\n\n\n\n\n");
        int newbookid=booksDAO.CreateBook(book.getIsbn(),book.getTitle());
//        return Response.created(new URI(String.valueOf(newbookid))).build();
        return "i created the ";
    }
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.APPLICATION_JSON)
    public HomeView createBook(@FormParam("isbn") int isbn,@FormParam("title") String title)
    {
        System.out.print("\n\n\n\n\n\n\n\n\n\nhelloabcdefgh\n\n\n\n\n\n");
        int newbookid=booksDAO.CreateBook(isbn,title);
        return new HomeView();

    }
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HomeView updateBook(@FormParam("isbn") int isbn,@FormParam("title") String title)
    {

        booksDAO.updateTitleByIsbn(title, isbn);
        URI uri2 = UriBuilder.fromUri("/books").build();
        Response response = Response.seeOther(uri2).build();
        throw new WebApplicationException(response);
        //return new HomeView();

    }
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HomeView updateBook(@FormParam("isbn") int isbn)
    {
        booksDAO.deleteByIsbn(isbn);

        return new HomeView();

    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("id") int isbn)
    {
        System.out.print("\n\n\n\n\n\n\n\n\n\nhelloabcd\n\n\n\n\n\n");
        booksDAO.updateTitleByIsbn("hgh", isbn);

        return Response.ok().build();
    }
    @GET
    @Path("/a")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks()
    {

        Book books=booksDAO.showAllBooks();
        return Response.ok(books).build();
    }




}
