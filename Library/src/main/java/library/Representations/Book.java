package library.Representations;

/**
 * Created by vaibhav.tomar on 23/07/15.
 */
public class Book {
    private final int isbn;
    private final String title;

    public Book()
    {
        isbn=0;
        title=null;

    }
    public Book(int isbn, String title)
    {
        this.isbn=isbn;
        this.title=title;

    }
    public int getIsbn()
    {
      return isbn;
    }
    public String getTitle()
    {
        return title;
    }


}
