package flipkart.pricing.apps.kaizen.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;

import javax.validation.constraints.NotNull;

public class SignalFetchDto {

    @JsonProperty
    @NotNull
    private String signalName;

    @JsonProperty
    @NotNull
    private String value;


    @JsonProperty
    @NotNull
    private SignalDataTypes signalType;

    public SignalFetchDto(String signalName, String value, SignalDataTypes signalType) {
        this.signalName = signalName;
        this.value = value;
        this.signalType = signalType;
    }

    public String getSignalName() {
        return signalName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SignalDataTypes getSignalType() {
        return signalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalFetchDto)) return false;

        SignalFetchDto that = (SignalFetchDto) o;

        if (signalName != null ? !signalName.equals(that.signalName) : that.signalName != null) return false;
        if (signalType != that.signalType) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = signalName != null ? signalName.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (signalType != null ? signalType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalFetchDto{" +
                "signalName='" + signalName + '\'' +
                ", value='" + value + '\'' +
                ", signalType=" + signalType +
                '}';
    }
}

