package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:31 AM
 * ListingPriceUpdateService : which hands over the request to Data layer simply(for now!)
 */
@Component
public class ListingPriceUpdatesServiceImpl implements ListingPriceUpdatesService {

    PricingAuditDataService pricingAuditDataService;

    @Inject
    public ListingPriceUpdatesServiceImpl(PricingAuditDataService pricingAuditDataService){
        this.pricingAuditDataService = pricingAuditDataService;
    }

    @Override
    public PriceComputationResultSet getFromVersion(Long version, int numberOfRecords) {
        return this.pricingAuditDataService.getPriceComputation(version,numberOfRecords);
    }
}
