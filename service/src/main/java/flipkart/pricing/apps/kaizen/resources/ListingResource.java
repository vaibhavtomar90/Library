package flipkart.pricing.apps.kaizen.resources;

import flipkart.pricing.apps.kaizen.api.SellerListingData;
import io.dropwizard.logging.LoggingFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @understands provides Http endpoint for Listing level data
 */
@Path("/listing")
@Component
public class ListingResource {

    public static final String CLIENT_ID_HEADER_KEY = "X-CLIENT-ID";
    private static Logger logger = LoggerFactory.getLogger(ListingResource.class);

    @POST
    @Path("/{id}")
    public String saveData(@PathParam("id") String listingId,
                           @HeaderParam(CLIENT_ID_HEADER_KEY) String clientId,
                           SellerListingData listingData) {
        logger.info("%s",listingData);
        return "OK";

    }
}
