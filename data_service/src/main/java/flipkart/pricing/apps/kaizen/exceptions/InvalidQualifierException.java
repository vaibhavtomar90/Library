package flipkart.pricing.apps.kaizen.exceptions;


public class InvalidQualifierException extends RuntimeException {
    public InvalidQualifierException() {
        super("Invalid qualifier provided");
    }
}
