package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.service.PricingAuditDataService;
import flipkart.pricing.apps.kaizen.service.PricingAuditDataServiceImpl;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;
import flipkart.pricing.apps.kaizen.service.datatypes.PricingData;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 14/05/15
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PricingAuditEventUtils {

    @Inject
    public PricingAuditEventUtils(PricingAuditDataService dataService){
        this.pricingAuditDataService = dataService;
    }

    PricingAuditDataService pricingAuditDataService;

    public ArrayList<ListingLatestPriceVersion> generateComputationRecords(
            String listingPatternPrefix, int noOfListing, int noOfPriceChangesPerListing) {

        PricingData pricingData = new PricingData(100.0, 150.0, 50.0);
        ArrayList<ListingLatestPriceVersion> arrayList = new ArrayList<>(noOfListing);

        for (int i = 0; i < noOfListing; ++i)
            arrayList.add(new ListingLatestPriceVersion("",0L,pricingData));  //this is bad.. ensureCapacity is not working!

        for (int j = 1; j < noOfPriceChangesPerListing+1; ++j) {

            for (int i = 0; i < noOfListing; ++i) {

                String listingID = listingPatternPrefix + new Integer(i).toString();
                PriceComputeEvent priceComputationEvent =
                        new PriceComputeEvent(listingID,
                                new Long(i * 1000 + j), pricingData, "TEXT BLAH BLUE BBELLEE");

                final Long priceVersion =
                        pricingAuditDataService.savePriceComputeRecord(priceComputationEvent);

                assertTrue(priceVersion > 0L);
                arrayList.set(i, new ListingLatestPriceVersion(listingID, priceVersion,pricingData));
            }
        }
        return arrayList;
    }
}
