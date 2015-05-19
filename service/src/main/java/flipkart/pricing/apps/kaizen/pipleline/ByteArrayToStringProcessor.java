package flipkart.pricing.apps.kaizen.pipleline;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.inject.Named;

@Named
public class ByteArrayToStringProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = new String((byte[]) exchange.getIn().getBody());
        exchange.getIn().setBody(body);
    }
}
