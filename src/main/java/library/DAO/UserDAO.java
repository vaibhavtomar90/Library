package library.DAO;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * Created by vaibhav.tomar on 25/07/15.
 */
public interface UserDAO {

    @SqlQuery("select * from User where userid=:userid")
    void showUser(@Bind("userid") int userid);
}
