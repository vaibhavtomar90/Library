package library.views;

import io.dropwizard.views.View;
import library.Representations.User;

/**
 * Created by vaibhav.tomar on 25/07/15.
 */
public class UserView extends View{

    public UserView()
    {
        super("users.ftl");
    }
    public UserView(User user)
    {
        super("users.ftl");
    }
}
