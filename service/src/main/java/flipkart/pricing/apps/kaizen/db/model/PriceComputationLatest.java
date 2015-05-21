package flipkart.pricing.apps.kaizen.db.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 08/05/15
 * Time: 12:29 AM
 *  Represents the table which stores the latest prices for a listing
 *  Table is used for ReConcililation API implementation.
 */
@Entity
public class PriceComputationLatest implements Serializable{

    @Id
    @Column(unique = true,columnDefinition = "char")
    String listingId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="priceVersion",unique = true)
    PriceComputationAudit priceComputationAudit;

    @Deprecated
    public PriceComputationLatest() {
    }

    public PriceComputationLatest(String listingId) {
        this.listingId = listingId;
    }

    public PriceComputationAudit getPriceComputationAudit() {
        return priceComputationAudit;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);

        builder.append("ID = "+listingId+ "name ="+ this.listingId);
        builder.append("  " + this.getPriceComputationAudit().toString());
        return builder.toString();
    }

    public void setPriceComputationAudit(PriceComputationAudit priceComputationAudit) {
        this.priceComputationAudit = priceComputationAudit;
    }

    public String getListingId() {
        return listingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceComputationLatest that = (PriceComputationLatest) o;

        if (!listingId.equals(that.listingId)) return false;
        if (!priceComputationAudit.equals(that.priceComputationAudit)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingId.hashCode();
        result = 31 * result + priceComputationAudit.hashCode();
        return result;
    }
}
