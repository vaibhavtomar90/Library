package library.DAO.mappers;

import library.Representations.Book;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vaibhav.tomar on 23/07/15.
 */
public class BooksMapper implements ResultSetMapper<Book> {
    public Book map(int index,ResultSet r,StatementContext ctx) throws SQLException
    {

        return new Book(r.getInt("isbn"),r.getString("title"));
    }
}
