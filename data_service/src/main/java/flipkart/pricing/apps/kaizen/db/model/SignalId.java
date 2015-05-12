package flipkart.pricing.apps.kaizen.db.model;


import flipkart.pricing.apps.kaizen.db.DomainEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SignalId implements Comparable<SignalId>, DomainEntity {

    @Column(name = "listing_id", nullable = false)
    private long listingId;

    @Column(name = "signal_type_id", nullable = false)
    private long signalTypeId;

    public SignalId() {}

    public SignalId(long listingId, long signalTypeId) {
        this.listingId = listingId;
        this.signalTypeId = signalTypeId;
    }

    public long getListingId() {
        return listingId;
    }


    public long getSignalTypeId() {
        return signalTypeId;
    }

    @Override
    public int compareTo(SignalId signalId) {
        int listingComparisonValue = Long.compare(listingId, signalId.getListingId());
        if (listingComparisonValue == 0) {
            return Long.compare(signalTypeId, signalId.getSignalTypeId());
        }
        return listingComparisonValue;
    }

    @Override
    public String toString() {
        return "SignalId{" +
                "listingId=" + listingId +
                ", signalTypeId=" + signalTypeId +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignalId)) return false;

        SignalId signalId = (SignalId) o;

        if (listingId != signalId.listingId) return false;
        if (signalTypeId != signalId.signalTypeId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (listingId ^ (listingId >>> 32));
        result = 31 * result + (int) (signalTypeId ^ (signalTypeId >>> 32));
        return result;
    }
}
