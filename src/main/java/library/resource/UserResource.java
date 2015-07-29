package library.resource;

import library.Representations.User;
import library.views.UserView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by vaibhav.tomar on 25/07/15.
 */
@Path("/users")
public class UserResource {
    public UserResource()
    {

    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    public UserView getUser(User user)
    {
        return new UserView(user);
    }
    @GET
    @Path("/login")
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_HTML)
    public String  usersession(@Context HttpServletRequest request)
    {

        System.out.print("i am in session");
        HttpSession session=request.getSession(true);
        Object abc=session.getAttribute("abc");
        if(abc!=null)
        {
            System.out.print(abc.toString());
        }


        return "i am nice person";
    }




}
