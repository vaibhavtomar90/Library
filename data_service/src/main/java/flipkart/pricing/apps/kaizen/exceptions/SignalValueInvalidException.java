package flipkart.pricing.apps.kaizen.exceptions;


public class SignalValueInvalidException extends RuntimeException {

    public SignalValueInvalidException(String value, String signalName) {
        super("The value "+value+" is invalid for "+signalName);
    }
}
