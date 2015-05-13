package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultset;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:31 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ListingPriceUpdatesServiceImpl implements ListingPriceUpdatesService {


    PricingAuditDataService pricingAuditDataService;

    @Inject
    public ListingPriceUpdatesServiceImpl(PricingAuditDataService pricingAuditDataService){
        this.pricingAuditDataService = pricingAuditDataService;
    }

    @Override
    public PriceComputationResultset getFromVersion(Long version, int numberOfRecords) {

        return this.pricingAuditDataService.getPriceComputation(version,numberOfRecords);

    }
}
