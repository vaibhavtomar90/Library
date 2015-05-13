package flipkart.pricing.apps.kaizen.exceptions;


public class SignalNameNotFoundException extends RuntimeException {

    public SignalNameNotFoundException(String signalName) {
        super("Signal "+signalName+" does not exist");
    }
}
