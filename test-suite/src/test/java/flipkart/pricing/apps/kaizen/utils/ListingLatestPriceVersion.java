package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.service.datatypes.PricingData;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 14/05/15
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListingLatestPriceVersion {

    String listingId;
    Long latestPriceVersion;

    PricingData pricingData;

    public ListingLatestPriceVersion(String listingId, Long latestPriceVersion, PricingData pricingData) {
        this.listingId = listingId;
        this.latestPriceVersion = latestPriceVersion;
        this.pricingData = pricingData;
    }

    public String getListingId() {
        return listingId;
    }

    public Long getLatestPriceVersion() {
        return latestPriceVersion;
    }

    public PricingData getPricingData() {
        return pricingData;
    }
}