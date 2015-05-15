package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Understands Dto to be shared for returning signals of a listing
 */
public class SignalFetchDto {

    @JsonProperty
    @NotNull
    private String listing;

    @JsonProperty
    @NotNull
    private Long version;

    @JsonProperty
    @NotNull
    private List<SignalFetchDetail> signals;

    @Deprecated
    SignalFetchDto() {} //for jackson

    public SignalFetchDto(String listing, Long version, List<SignalFetchDetail> signals) {
        this.listing = listing;
        this.version = version;
        this.signals = signals;
    }

    public String getListing() {
        return listing;
    }

    public Long getVersion() {
        return version;
    }

    public List<SignalFetchDetail> getSignals() {
        return signals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalFetchDto)) return false;

        SignalFetchDto that = (SignalFetchDto) o;

        if (listing != null ? !listing.equals(that.listing) : that.listing != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null)
            return false;
        if (signals != null ? !signals.equals(that.signals) : that.signals != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listing != null ? listing.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (signals != null ? signals.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalFetchDto{" +
                "listing='" + listing + '\'' +
                ", version=" + version +
                ", signals=" + signals +
                '}';
    }
}
