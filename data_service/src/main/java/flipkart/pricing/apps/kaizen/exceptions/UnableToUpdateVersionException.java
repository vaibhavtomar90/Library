package flipkart.pricing.apps.kaizen.exceptions;


public class UnableToUpdateVersionException extends RuntimeException {

    public UnableToUpdateVersionException(String listing) {
        super("Unable to increment version for "+listing+" , probably listing is not present");
    }
}
