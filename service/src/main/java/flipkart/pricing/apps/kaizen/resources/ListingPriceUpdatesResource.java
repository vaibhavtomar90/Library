package flipkart.pricing.apps.kaizen.resources;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */

import flipkart.pricing.apps.kaizen.api.PricingDeltaUpdateResponseDTO;
import flipkart.pricing.apps.kaizen.api.PricingUpdate;
import flipkart.pricing.apps.kaizen.service.ListingPriceUpdatesService;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;
import flipkart.pricing.apps.kaizen.service.datatypes.PricingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @understands Provides REST Http endpoint for serving the listingPriceUpdates Delta API
 */
@Path("/v1/listings/")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListingPriceUpdatesResource {

    public static final String CLIENT_ID_HEADER_KEY = "X-CLIENT-ID";
    private static final int MAX_DELTA_RECORDS = 1000;
    private static Logger logger = LoggerFactory.getLogger(ListingPriceUpdatesResource.class);

    ListingPriceUpdatesService listingPriceUpdatesService;

    @Inject
    public ListingPriceUpdatesResource(ListingPriceUpdatesService listingPriceUpdatesService) {
        this.listingPriceUpdatesService = listingPriceUpdatesService;
    }

    @GET
    @Path("/delta")
    public PricingDeltaUpdateResponseDTO getDeltaPriceUpdates
            (@QueryParam("version")
             Long fromVersion,
             @QueryParam("count")
             int updateCount,
             @HeaderParam(CLIENT_ID_HEADER_KEY) String clientId)
    {
        try {

            // TODO : add more client level validation, based on X-Client-ID

            if (!validateQueryParameters(fromVersion, updateCount))
                throw new IllegalArgumentException("Incorrect fromVersion or Count specified");

            final PriceComputationResultSet computationResultset =
                   this.listingPriceUpdatesService.getFromVersion(fromVersion, updateCount);

            return map(computationResultset);

        }
        catch (IllegalArgumentException ex) {
            logger.error("Exception ",ex);
            throw new WebApplicationException("Invalid Query Parameter specified", Response.Status.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Exception ",ex);
            throw new WebApplicationException(ex); //HTTP/500
        }
    }

    // Helper mapper functions //

    private Boolean validateQueryParameters(Long fromVersion, int updateCount) {
        return fromVersion >= 0 && updateCount <= MAX_DELTA_RECORDS;
    }

    private PricingDeltaUpdateResponseDTO map(PriceComputationResultSet computationResultset) {

        List<PricingUpdate> pricingUpdateList = new ArrayList<>(computationResultset.getPriceComputations().size());

        for (PriceComputation pc : computationResultset.getPriceComputations())
            pricingUpdateList.add(map(pc));

        return new PricingDeltaUpdateResponseDTO(pricingUpdateList,
                computationResultset.getNextAvailableVersion(),
                computationResultset.getHasMoreToRead());
    }

    private PricingUpdate map(PriceComputation pc) {
        PricingData pricingData = pc.getPricingData();

        return new PricingUpdate(pc.getListingID(), pricingData.getMrp(),
                pricingData.getFsp(),
                pricingData.getFk_discount(), pc.getPriceVersion());
    }
}

