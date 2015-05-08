package flipkart.pricing.apps.kaizen.exceptions;


public class UnableToUpdateVersionException extends RuntimeException {

    public UnableToUpdateVersionException() {
        super("Unable to increment version, probably listing is not present");
    }
}
