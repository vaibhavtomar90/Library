package library.Representations;

/**
 * Created by vaibhav.tomar on 25/07/15.
 */
public class User {
    private int userid;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    public User()
    {

    }
    public User(int userid, String firstname, String lastname)
    {
        this.userid=userid;
        this.firstname=firstname;
        this.lastname=lastname;
    }
    public User getUserById(int id)
    {
        return new User();
    }
}
