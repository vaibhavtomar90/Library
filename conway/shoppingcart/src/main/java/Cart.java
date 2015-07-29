import java.util.LinkedList;
import java.util.List;

/**
 * Created by vaibhav.tomar on 21/07/15.
 */
public class Cart {
    private List<Integer> productids;

    public Cart()
    {
        productids=new LinkedList<Integer>();
    }
    public void additem(int productid)
    {

        productids.add(new Integer(productid));
    }
    public void deleteitem(int productid)
    {

        int index=productids.indexOf(new Integer(productid));
        productids.remove(index);
    }
}
