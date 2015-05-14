package flipkart.pricing.apps.kaizen.db.model;


import flipkart.pricing.apps.kaizen.db.Model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class SignalInfo implements Comparable<SignalInfo>, Model {

    //TODO Is there a better way to include relation with listingInfo and signalType here ??
    @EmbeddedId
    private SignalId id;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private long version;

    @Column
    private Timestamp serverTimestamp;

    @Column
    private String qualifier;

    @Deprecated
    SignalInfo() {}


    public SignalInfo(SignalId id, String value, long version, String qualifier) {
        this.id = id;
        this.value = value;
        this.version = version;
        this.qualifier = qualifier;
    }

    public SignalInfo(SignalId id, String value, long version) {
        this(id, value, version, null);
    }

    public SignalInfo(Long listingId, Long signalTypeId, String value, long version) {
        this(new SignalId(listingId, signalTypeId), value, version);
    }

    public SignalInfo(Long listingId, Long signalTypeId, String value, long version, String qualifier) {
        this(new SignalId(listingId, signalTypeId), value, version, qualifier);
    }


    public SignalId getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public long getVersion() {
        return version;
    }

    public Timestamp getServerTimestamp() {
        return serverTimestamp;
    }

    public String getQualifier() {
        return qualifier;
    }

    public int compareTo(SignalInfo signalInfo) {
        return id.compareTo(signalInfo.getId());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalInfo)) return false;

        SignalInfo signalInfo = (SignalInfo) o;

        if (version != signalInfo.version) return false;
        if (qualifier != null ? !qualifier.equals(signalInfo.qualifier) : signalInfo.qualifier != null) return false;
        if (value != null ? !value.equals(signalInfo.value) : signalInfo.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SignalInfo{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", version=" + version +
                ", serverTimestamp=" + serverTimestamp +
                ", qualifier='" + qualifier + '\'' +
                '}';
    }
}
