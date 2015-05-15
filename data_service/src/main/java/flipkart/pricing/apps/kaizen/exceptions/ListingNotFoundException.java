package flipkart.pricing.apps.kaizen.exceptions;


public class ListingNotFoundException extends RuntimeException {

    public ListingNotFoundException(String listing) {
        super("Listing "+listing+" not found");
    }
}
