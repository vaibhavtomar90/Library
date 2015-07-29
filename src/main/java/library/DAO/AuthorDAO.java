package library.DAO;

import library.DAO.mappers.AuthorMapper;
import library.Representations.Author;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

/**
 * Created by vaibhav.tomar on 27/07/15.
 */
@RegisterMapper(AuthorMapper.class)
public interface AuthorDAO {

    @SqlQuery("select * from authors")
    public List<Author> showAuthors();

    @SqlUpdate("insert into authors values(:authorid,:firstname,:lastname )")
    public void createAuthor(@Bind("authorid") int authorid,
                             @Bind("firstname") String firstname,
                             @Bind("lastname") String lastname);
    @SqlUpdate("update authors set firstname= :firstname,lastname= :lastname where authorid= :authorid")
    public void updateAuthor(@Bind("authorid")  int authorid,
                             @Bind("firstname") String firstname,
                             @Bind("lastname") String lastname
                             );
    @SqlUpdate("delete from authors where authorid= :authorid")
    public void deleteAuthor(@Bind("authorid") int authorid);
}
