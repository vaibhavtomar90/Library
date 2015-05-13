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
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultset;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @understands provides Http endpoint for serving the listingPriceUpdates
 */
@Path("/v1/listings/")
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ListingPriceUpdatesResource {

    public static final String CLIENT_ID_HEADER_KEY = "X-CLIENT-ID";
    private static Logger logger = LoggerFactory.getLogger(ListingPriceUpdatesResource.class);

    ListingPriceUpdatesService listingPriceUpdatesService;

    @Inject
    public ListingPriceUpdatesResource(ListingPriceUpdatesService listingPriceUpdatesService) {
        this.listingPriceUpdatesService = listingPriceUpdatesService;
    }

    @POST
    @Path("/delta")
    public PricingDeltaUpdateResponseDTO getDeltaPriceUpdates
            (@QueryParam("version") @Min(0)
             Long fromVersion,
             @QueryParam("count") @Max(100) @Min(1)
             int updateCount,
             @HeaderParam(CLIENT_ID_HEADER_KEY) String clientId)
    {
        final PriceComputationResultset computationResultset =
                this.listingPriceUpdatesService.getFromVersion(fromVersion, updateCount);

        return map(computationResultset);
    }

    // Helper mapper functions

    private PricingDeltaUpdateResponseDTO map(PriceComputationResultset computationResultset) {


        List<PricingUpdate> pricingUpdateList = new ArrayList<>(computationResultset.getPriceComputations().size());

        for (PriceComputation pc : computationResultset.getPriceComputations()) {
            pricingUpdateList.add(map(pc));
        }
        return new PricingDeltaUpdateResponseDTO(pricingUpdateList, computationResultset.getNextAvailableVersion(),
                computationResultset.getHasMoreToRead());
    }

    private PricingUpdate map(PriceComputation pc) {
        return new PricingUpdate(pc.getListingID(), pc.getMrp(), pc.getFsp(), pc.getFk_discount(), pc.getPriceVersion());
    }
}

