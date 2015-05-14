package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 14/05/15
 * Time: 12:18 AM
 * Description: This POJO is used by other interacting systems to write the computation activity
 * to the audit Datastore. Its a per listing object, per price change.
 */
public class PriceComputeEvent implements Serializable {

    /**
     * ListingID
     */
    String listingID;

    /**
     * The DM's dataversion for this listing, which
     * triggered this price computation event
     */
    Long dataVersion;

    /**
     * The priceing data, to be bundled, across!
     */
    PricingData pricingData;

    String jsonContext;

    public PriceComputeEvent(String listingID, Long dataVersion,
                             PricingData pricingData, String jsonContext) {
        this.listingID = listingID;
        this.dataVersion = dataVersion;
        this.pricingData = pricingData;
        this.jsonContext = jsonContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceComputeEvent)) return false;

        PriceComputeEvent that = (PriceComputeEvent) o;

        if (!dataVersion.equals(that.dataVersion)) return false;
        if (!listingID.equals(that.listingID)) return false;
        if (!pricingData.equals(that.pricingData)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingID.hashCode();
        result = 31 * result + dataVersion.hashCode();
        result = 31 * result + pricingData.hashCode();
        return result;
    }

    public String getListingID() {

        return listingID;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public PricingData getPricingData() {
        return pricingData;
    }

    public String getJsonContext() {
        return jsonContext;
    }
}
