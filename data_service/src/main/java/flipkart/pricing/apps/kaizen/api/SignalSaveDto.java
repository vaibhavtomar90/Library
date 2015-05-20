package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Understands Dto to be shared for signal ingestion of a listing
 */
public class SignalSaveDto {

    @JsonProperty
    @NotNull
    private String listing;

    @JsonProperty
    @NotNull
    @NotEmpty
    @Valid
    private List<SignalSaveDetail> signals;

    @JsonProperty
    private Long version;

    @Deprecated
    SignalSaveDto() {} //for jackson


    public SignalSaveDto(String listing, List<SignalSaveDetail> signals, Long version) {
        this.listing = listing;
        this.signals = signals;
        this.version = version;
    }


    public String getListing() {
        return listing;
    }

    public List<SignalSaveDetail> getSignals() {
        return signals;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalSaveDto)) return false;

        SignalSaveDto that = (SignalSaveDto) o;

        if (listing != null ? !listing.equals(that.listing) : that.listing != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null)
            return false;
        if (signals != null ? !signals.equals(that.signals) : that.signals != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listing != null ? listing.hashCode() : 0;
        result = 31 * result + (signals != null ? signals.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalSaveDto{" +
                "listing='" + listing + '\'' +
                ", signals=" + signals +
                ", version=" + version +
                '}';
    }
}
