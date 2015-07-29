package library.Representations;

/**
 * Created by vaibhav.tomar on 23/07/15.
 */
public class Author {
    private int authorid;
    private String firstName;
    private String lastName;

    public Author()
    {
        authorid=0;
        firstName=null;
        lastName=null;
    }
    public Author(int authorid,String firstName,String lastName)
    {
        this.authorid=authorid;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public int getAuthorId()
    {
        return authorid;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public  String getLastName()
    {
        return lastName;
    }
}
