package flipkart.pricing.apps.kaizen.resources;

import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @understands provides Http endpoint for Listing level data
 */
@Path("/v1")
@Named
public class ListingResource {

    public static final String CLIENT_ID_HEADER_KEY = "X-CLIENT-ID";
    private static final Logger logger = LoggerFactory.getLogger(ListingResource.class);

    private final ProducerTemplate persistEndpoint;

    @Inject
    public ListingResource(@Named("incomingSignal") ProducerTemplate persistEndpoint) {
        this.persistEndpoint = persistEndpoint;
    }

    @POST
    @Path("/listings")
    public String saveData(@HeaderParam(CLIENT_ID_HEADER_KEY) String clientId,
                           SignalSaveDto listingData) {
        logger.debug("Received {}",listingData);
        persistEndpoint.sendBody(listingData);
        return "OK";

    }
}
