package flipkart.pricing.apps.kaizen.db.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 08/05/15
 * Time: 12:29 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="PriceReConComputation")
public class ReConAuditRecord implements Serializable{

    @Id
    @Column(name="listing_id", unique = true,columnDefinition = "char")
    String listingId;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="price_version",unique = true)
    PricingAuditRecord pricingAuditRecord;

    @Deprecated
    public ReConAuditRecord() {
    }

    public ReConAuditRecord(String listingId) {
        this.listingId = listingId;
    }

    public PricingAuditRecord getPricingAuditRecord() {
        return pricingAuditRecord;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(100);

        builder.append("ID = "+listingId+ "name ="+ this.listingId);
        builder.append("  " + this.getPricingAuditRecord().toString());
        return builder.toString();
    }

    public void setPricingAuditRecord(PricingAuditRecord pricingAuditRecord) {
        this.pricingAuditRecord = pricingAuditRecord;
    }

    public String getListingId() {
        return listingId;
    }
}
