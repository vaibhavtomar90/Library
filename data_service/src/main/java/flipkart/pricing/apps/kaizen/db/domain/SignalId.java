package flipkart.pricing.apps.kaizen.db.domain;


import flipkart.pricing.apps.kaizen.db.DomainEntity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SignalId implements Comparable<SignalId>, DomainEntity {

    @Column(name = "listing_id", nullable = false)
    private Long listingId;

    @Column(name = "signal_type_id", nullable = false)
    private Long signalTypeId;

    public SignalId(Long listingId, Long signalTypeId) {
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
        int listingComparisonValue = listingId.compareTo(signalId.getListingId());
        if (listingComparisonValue == 0) {
            return signalTypeId.compareTo(signalId.getSignalTypeId());
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

        if (listingId != null ? !listingId.equals(signalId.listingId) : signalId.listingId != null) return false;
        if (signalTypeId != null ? !signalTypeId.equals(signalId.signalTypeId) : signalId.signalTypeId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingId != null ? listingId.hashCode() : 0;
        result = 31 * result + (signalTypeId != null ? signalTypeId.hashCode() : 0);
        return result;
    }
}
