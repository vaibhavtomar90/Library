package flipkart.pricing.apps.kaizen.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * @understands Listing Data from the Seller
 */
public class SellerListingData {
    @Valid
    @NotNull
    @JsonProperty
    private Double mrp;

    @Valid
    @NotNull
    @JsonProperty
    private Double ssp;

    @Valid
    @NotNull
    @JsonProperty
    private Long version;

    //TODO : Figure eventId

    @Deprecated // for jackson
    public SellerListingData() {
    }

    public SellerListingData(Double mrp, Double ssp, Long version) {
        this.mrp = mrp;
        this.ssp = ssp;
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SellerListingData{");
        sb.append("mrp=").append(mrp);
        sb.append(", ssp=").append(ssp);
        sb.append(", version=").append(version);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SellerListingData that = (SellerListingData) o;
        return Objects.equals(mrp, that.mrp) &&
            Objects.equals(ssp, that.ssp) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mrp, ssp, version);
    }
}
