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

    @Column(nullable = false)
    private Timestamp expiry;

    @Column(name = "server_timestamp", nullable = false)
    private Timestamp serverTimestamp;

    public Signal() {}

    public Signal(SignalId id, String value, long version, Timestamp expiry, Timestamp serverTimestamp) {
        this(id.getListingId(), id.getSignalTypeId(), value, version, expiry, serverTimestamp);
    }

    public Signal(Long listingId, Long signalTypeId, String value, long version, Timestamp expiry, Timestamp serverTimestamp) {
        this.id = new SignalId(listingId, signalTypeId);
        this.value = value;
        this.version = version;
        this.expiry = expiry;
        this.serverTimestamp = serverTimestamp;
    }

    public Signal(SignalId id, String value, long version, Timestamp expiry) {
        this(id.getListingId(), id.getSignalTypeId(), value, version, expiry, null);
    }

    public Signal(Long listingId, Long signalTypeId, String value, long version, Timestamp expiry) {
        this(listingId, signalTypeId, value, version, expiry, null);
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

    public Timestamp getExpiry() {
        return expiry;
    }

    public Timestamp getServerTimestamp() {
        return serverTimestamp;
    }

    @Override
    public int compareTo(Signal signal) {
        return id.compareTo(signal.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Signal)) return false;

        Signal signal = (Signal) o;

        if (version != signal.version) return false;
        if (expiry != null ? !expiry.equals(signal.expiry) : signal.expiry != null) return false;
        if (id != null ? !id.equals(signal.id) : signal.id != null) return false;
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
        result = 31 * result + (expiry != null ? expiry.hashCode() : 0);
        result = 31 * result + (serverTimestamp != null ? serverTimestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Signal{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", version=" + version +
                ", expiry=" + expiry +
                ", serverTimestamp=" + serverTimestamp +
                '}';
    }
}
