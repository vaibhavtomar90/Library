package flipkart.pricing.apps.kaizen.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:55 AM
 * Description : Individual priceupdate item
 */
public class PricingUpdate {

    @JsonProperty
    private final String listing;

    @JsonProperty
    private final Double mrp;

    @JsonProperty
    private final Double fsp;

    @JsonProperty
    private final Double fk_discount;

    @JsonProperty
    private final Long version;

    @JsonCreator
    public PricingUpdate(@JsonProperty("listing") String listing,
                         @JsonProperty("mrp") Double mrp,
                         @JsonProperty("fsp") Double fsp,
                         @JsonProperty("fk_discount") Double fk_discount,
                         @JsonProperty("version") Long version){
        this.listing = listing;
        this.mrp = mrp;
        this.fsp = fsp;
        this.fk_discount = fk_discount;
        this.version = version;
    }

    public String getListing() {
        return listing;
    }

    public Double getMrp() {
        return mrp;
    }

    public Double getFsp() {
        return fsp;
    }

    public Double getFk_discount() {
        return fk_discount;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(100);
        stringBuilder.append(getClass().getSimpleName()).append("[").append("listing={").append(listing)
                .append("}").append("mrp={").append(mrp).append("}").append("fsp=").append("{").append(fsp).append("}")
                .append("fk_discount=").append("{").append(fk_discount).append("}").append("version={")
                .append(version) .append("}").append("]") ;
        return stringBuilder.toString();
    }
}
