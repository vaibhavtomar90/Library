package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SignalResponseDto {

    @JsonProperty("listing")
    @NotNull
    private String listing;

    @JsonProperty("version")
    @NotNull
    private Long listingVersion;

    @JsonProperty("signals")
    @NotNull
    private List<SignalResponseDetail> signals;

    public SignalResponseDto(String listing, Long listingVersion, List<SignalResponseDetail> signals) {
        this.listing = listing;
        this.listingVersion = listingVersion;
        this.signals = signals;
    }

    public String getListing() {
        return listing;
    }

    public Long getListingVersion() {
        return listingVersion;
    }

    public List<SignalResponseDetail> getSignals() {
        return signals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalResponseDto)) return false;

        SignalResponseDto that = (SignalResponseDto) o;

        if (listing != null ? !listing.equals(that.listing) : that.listing != null) return false;
        if (listingVersion != null ? !listingVersion.equals(that.listingVersion) : that.listingVersion != null)
            return false;
        if (signals != null ? !signals.equals(that.signals) : that.signals != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listing != null ? listing.hashCode() : 0;
        result = 31 * result + (listingVersion != null ? listingVersion.hashCode() : 0);
        result = 31 * result + (signals != null ? signals.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalResponseDto{" +
                "listing='" + listing + '\'' +
                ", listingVersion=" + listingVersion +
                ", signals=" + signals +
                '}';
    }
}
