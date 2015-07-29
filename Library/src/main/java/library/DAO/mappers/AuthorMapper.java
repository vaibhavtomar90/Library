package library.DAO.mappers;

import library.Representations.Author;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by vaibhav.tomar on 27/07/15.
 */
public class AuthorMapper implements ResultSetMapper<Author> {
        public Author map(int index,ResultSet r,StatementContext ctx) throws SQLException
        {
            return new Author(r.getInt("authorid"),r.getString("firstname"),r.getString("lastname"));
        }



}
