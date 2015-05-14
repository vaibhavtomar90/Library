package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;

import javax.validation.constraints.NotNull;

/**
 * @Understands the individual signal details (name, value, etc) returned for a listing
 */
public class SignalFetchDetail {

    @JsonProperty
    @NotNull
    private String name;

    @JsonProperty
    @NotNull
    private String value;

    @JsonProperty
    @NotNull
    private SignalDataType dataType;

    @Deprecated
    SignalFetchDetail() {} //for jackson

    public SignalFetchDetail(String name, String value, SignalDataType dataType) {
        this.name = name;
        this.value = value;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public SignalDataType getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalFetchDetail)) return false;

        SignalFetchDetail that = (SignalFetchDetail) o;

        if (dataType != that.dataType) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalFetchDetail{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", dataType=" + dataType +
                '}';
    }
}
