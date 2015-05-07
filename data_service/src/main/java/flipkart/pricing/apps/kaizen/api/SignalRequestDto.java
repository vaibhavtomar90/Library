package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class SignalRequestDto {

    @JsonProperty("listing")
    @NotNull
    private String listing;

    @JsonProperty("signals")
    @NotNull
    @NotEmpty
    @Valid
    private List<SignalRequestDetail> signalRequestDetails;

    @JsonProperty("version")
    private Long listingVersion;


    public SignalRequestDto(String listing, List<SignalRequestDetail> signalRequestDetails, Long listingVersion) {
        this.listing = listing;
        this.signalRequestDetails = signalRequestDetails;
        this.listingVersion = listingVersion;
    }

    public SignalRequestDto(String listing, List<SignalRequestDetail> signalRequestDetails) {
       this(listing, signalRequestDetails, null);
    }


    public String getListing() {
        return listing;
    }

    public List<SignalRequestDetail> getSignalRequestDetails() {
        return signalRequestDetails;
    }

    public Long getListingVersion() {
        return listingVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalRequestDto)) return false;

        SignalRequestDto that = (SignalRequestDto) o;

        if (listing != null ? !listing.equals(that.listing) : that.listing != null) return false;
        if (listingVersion != null ? !listingVersion.equals(that.listingVersion) : that.listingVersion != null)
            return false;
        if (signalRequestDetails != null ? !signalRequestDetails.equals(that.signalRequestDetails) : that.signalRequestDetails != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listing != null ? listing.hashCode() : 0;
        result = 31 * result + (signalRequestDetails != null ? signalRequestDetails.hashCode() : 0);
        result = 31 * result + (listingVersion != null ? listingVersion.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalRequestDto{" +
                "listing='" + listing + '\'' +
                ", signalRequestDetails=" + signalRequestDetails +
                ", listingVersion=" + listingVersion +
                '}';
    }
}
