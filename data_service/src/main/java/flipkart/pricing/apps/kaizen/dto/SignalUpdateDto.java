package flipkart.pricing.apps.kaizen.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class SignalUpdateDto {

    @JsonProperty
    @NotNull
    private String signalName;

    @JsonProperty
    @NotNull
    private String signalValue;

    @JsonProperty
    @NotNull
    private Long version;

    @JsonProperty
    @NotNull
    private Timestamp signalExpiry;

    public SignalUpdateDto(String signalName, String signalValue, Long version, Timestamp signalExpiry) {
        this.signalName = signalName;
        this.signalValue = signalValue;
        this.version = version;
        this.signalExpiry = signalExpiry;
    }

    public String getSignalName() {
        return signalName;
    }

    public String getSignalValue() {
        return signalValue;
    }

    public Long getVersion() {
        return version;
    }

    public Timestamp getSignalExpiry() {
        return signalExpiry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalUpdateDto)) return false;

        SignalUpdateDto that = (SignalUpdateDto) o;

        if (signalExpiry != null ? !signalExpiry.equals(that.signalExpiry) : that.signalExpiry != null) return false;
        if (signalName != null ? !signalName.equals(that.signalName) : that.signalName != null) return false;
        if (signalValue != null ? !signalValue.equals(that.signalValue) : that.signalValue != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = signalName != null ? signalName.hashCode() : 0;
        result = 31 * result + (signalValue != null ? signalValue.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (signalExpiry != null ? signalExpiry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalUpdateDto{" +
                "signalName='" + signalName + '\'' +
                ", signalValue='" + signalValue + '\'' +
                ", version=" + version +
                ", signalExpiry=" + signalExpiry +
                '}';
    }
}


