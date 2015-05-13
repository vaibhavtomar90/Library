package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;

import javax.validation.constraints.NotNull;

public class SignalResponseDetail {

    @JsonProperty
    @NotNull
    private String name;

    @JsonProperty
    @NotNull
    private String value;

    @JsonProperty
    @NotNull
    private SignalDataTypes dataType;

    @Deprecated
    SignalResponseDetail() {} //for jackson

    public SignalResponseDetail(String name, String value, SignalDataTypes dataType) {
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

    public SignalDataTypes getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalResponseDetail)) return false;

        SignalResponseDetail that = (SignalResponseDetail) o;

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
        return "SignalResponseDetail{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", dataType=" + dataType +
                '}';
    }
}
