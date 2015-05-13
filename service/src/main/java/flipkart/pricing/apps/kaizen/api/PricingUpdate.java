package flipkart.pricing.apps.kaizen.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class PricingUpdate {

    @JsonProperty
    final String listing;

    @JsonProperty
    final Double mrp;

    @JsonProperty
    final Double fsp;

    @JsonProperty
    final Double fk_discount;

    @JsonProperty
    final Long version;

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
}
