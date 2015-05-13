package flipkart.pricing.apps.kaizen.api;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class SignalRequestDetail {

    @JsonProperty
    @NotNull
    private String name;

    @JsonProperty
    @NotNull
    private String value;

    @JsonProperty
    @NotNull
    private Long version;


    @JsonProperty
    private String qualifier;

    @Deprecated
    SignalRequestDetail() {} //for jackson


    public SignalRequestDetail(String name, String value, Long version, String qualifier) {
        this.name = name;
        this.value = value;
        this.version = version;
        this.qualifier = qualifier;
    }

    public SignalRequestDetail(String name, String value, Long version) {
        this(name, value, version, null);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Long getVersion() {
        return version;
    }

    public String getQualifier() {
        return qualifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalRequestDetail)) return false;

        SignalRequestDetail that = (SignalRequestDetail) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (qualifier != null ? !qualifier.equals(that.qualifier) : that.qualifier != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalRequestDetail{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", version=" + version +
                ", qualifier='" + qualifier + '\'' +
                '}';
    }
}
