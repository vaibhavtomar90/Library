package library.DAO;

import library.DAO.mappers.BooksMapper;
import library.Representations.Book;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

/**
 * Created by vaibhav.tomar on 23/07/15.
 */
@RegisterMapper(BooksMapper.class)
public interface BooksDAO {


    //@SqlQuery("select title from Books where isbn = :isbn")
    @SqlUpdate("update books set title= :title where isbn= :isbn")
    void updateTitleByIsbn(@Bind("title") String title,@Bind("isbn") int isbn);

    @SqlUpdate("delete from books where isbn= :isbn")
    void deleteByIsbn(@Bind("isbn") int isbn);
    @SqlQuery("select * from Books")
    Book showAllBooks();
    @GetGeneratedKeys
    @SqlUpdate("insert into Books values (:isbn, :title)")
    int CreateBook(@Bind("isbn") int isbn,@Bind("title") String title);

}
