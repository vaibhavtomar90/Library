package library.views;

import io.dropwizard.views.View;

/**
 * Created by vaibhav.tomar on 27/07/15.
 */
public class AuthorView extends View{

    public AuthorView()
    {
        super("authors.ftl");
    }
}
