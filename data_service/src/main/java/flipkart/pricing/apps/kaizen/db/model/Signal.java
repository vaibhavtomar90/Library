package flipkart.pricing.apps.kaizen.db.model;


import flipkart.pricing.apps.kaizen.db.DomainEntity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "signals")
public class Signal implements Comparable<Signal>, DomainEntity {

    @EmbeddedId
    private SignalId id;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private long version;

    @Column(name = "server_timestamp", nullable = false)
    private Timestamp serverTimestamp;

    @Column
    private String qualifier;


    public Signal(SignalId id, String value, long version, Timestamp serverTimestamp, String qualifier) {
        this.id = id;
        this.value = value;
        this.version = version;
        this.serverTimestamp = serverTimestamp;
        this.qualifier = qualifier;
    }

    public Signal(SignalId id, String value, long version, Timestamp serverTimestamp) {
        this(id, value, version, serverTimestamp, null);
    }

    public Signal(Long listingId, Long signalTypeId, String value, long version, Timestamp serverTimestamp, String qualifier) {
        this(new SignalId(listingId, signalTypeId), value, version, serverTimestamp, qualifier);
    }

    public Signal(Long listingId, Long signalTypeId, String value, long version, Timestamp serverTimestamp) {
        this(listingId, signalTypeId, value, version, serverTimestamp, null);
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

    public int compareTo(Signal signal) {
        return id.compareTo(signal.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signal)) return false;

        Signal signal = (Signal) o;

        if (version != signal.version) return false;
        if (id != null ? !id.equals(signal.id) : signal.id != null) return false;
        if (qualifier != null ? !qualifier.equals(signal.qualifier) : signal.qualifier != null) return false;
        if (serverTimestamp != null ? !serverTimestamp.equals(signal.serverTimestamp) : signal.serverTimestamp != null)
            return false;
        if (value != null ? !value.equals(signal.value) : signal.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (serverTimestamp != null ? serverTimestamp.hashCode() : 0);
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", version=" + version +
                ", serverTimestamp=" + serverTimestamp +
                ", qualifier='" + qualifier + '\'' +
                '}';
    }
}
