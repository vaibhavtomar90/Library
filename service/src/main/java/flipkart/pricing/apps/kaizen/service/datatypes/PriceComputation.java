package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:33 PM
 * Description : Domain POJO to represent the single Computation record
 * retrieved from DB (for a Version per listingID)
 */
public class PriceComputation implements Serializable {

    private final String listingID;

    /**
     * The unique global counter version, for this priceComputation!
     */
    private final Long priceVersion;

    private final PricingData pricingData;

    private final String computeContext;

    private final Timestamp computedAt;

    public PriceComputation(String listingID, Long priceVersion,
                            PricingData pricingData,
                            Timestamp timestamp,
                            String computeContext) {
        this.listingID = listingID;
        this.priceVersion = priceVersion;
        this.pricingData = pricingData;
        this.computedAt = timestamp;
        this.computeContext = computeContext;
    }

    public String getComputeContext() {
        return computeContext;
    }

    public String getListingID() {
        return listingID;
    }

    public Long getPriceVersion() {
        return priceVersion;
    }

    public Timestamp getComputedAt() {
        return computedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceComputation)) return false;

        PriceComputation that = (PriceComputation) o;

        if (!listingID.equals(that.listingID)) return false;
        if (!priceVersion.equals(that.priceVersion)) return false;
        if (!pricingData.equals(that.pricingData)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingID.hashCode();
        result = 31 * result + priceVersion.hashCode();
        return result;
    }

    public PricingData getPricingData() {
        return pricingData;
    }
}

