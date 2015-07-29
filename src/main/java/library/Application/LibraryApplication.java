package library.Application;

import com.bazaarvoice.dropwizard.redirect.PathRedirect;
import com.bazaarvoice.dropwizard.redirect.RedirectBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import library.config.LibConfig;
import library.resource.*;
import org.skife.jdbi.v2.DBI;

/**
 * Created by vaibhav.tomar on 22/07/15.
 */

public class LibraryApplication extends Application<LibConfig> {

    public static void main(String[] args) throws Exception
    {

        new LibraryApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<LibConfig> bootstrap)
    {
        bootstrap.addBundle(new RedirectBundle(
                new PathRedirect("/", "/home")
        ));
        bootstrap.addBundle(new AssetsBundle("/assets/", "/Assets/"));
        bootstrap.addBundle(new ViewBundle<LibConfig>());
    }


    @Override
    public void run(LibConfig configuration, Environment environment)
    {
        System.out.print("\n\n\n\n\n\nHello\n\n\n\n\n\n\n");
        final LibraryResource libraryResource=new LibraryResource();
        final HomeResource homeResource =new HomeResource();
        final BookResource booksResource=new BookResource();
        final UserResource userResource=new UserResource();
        final AuthorResource authorResource=new AuthorResource();
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        //final BooksDAO booksDAO = jdbi.onDemand(BooksDAO.class);
        //final AuthorDAO authorDAO = jdbi.onDemand(AuthorDAO.class);
        environment.jersey().register(new BookResource(jdbi));
        environment.jersey().register(new AuthorResource(jdbi));
        environment.jersey().register(libraryResource);
        environment.jersey().register(homeResource);
        environment.jersey().register(booksResource);
        environment.jersey().register(userResource);
        environment.jersey().register(authorResource);




    }

}
