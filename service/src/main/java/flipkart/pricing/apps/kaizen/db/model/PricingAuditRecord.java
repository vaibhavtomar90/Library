package flipkart.pricing.apps.kaizen.db.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 08/05/15
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */


@Entity
@Table(name="PriceComputationAudit")
public class PricingAuditRecord implements Serializable{


    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "pricingAuditRecord", fetch = FetchType.LAZY)
    // @JoinColumn(name="listing_id",unique = true)
    // ReConAuditRecord reConAuditRecord;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="price_version")
    Long id;

    @Column(name="listing_id", columnDefinition = "char", length = 26)
    String listingId;


    @Column(name="mrp", nullable = false)
    double mrp ;

    @Column(name="fsp", nullable = false)
    double fsp;

    @Column(name="fk_discount", nullable = false)
    double fk_discount ;

    @Column(name="computedAt")
    Timestamp computedAt;

    @Column(name="jsonContext")
    String jsonContext;


    public PricingAuditRecord(String listingId, double mrp, double fsp, double fk_discount) {
        this.listingId = listingId;
        this.mrp = mrp;
        this.fsp = fsp;
        this.fk_discount = fk_discount;
    }

    public PricingAuditRecord() {

    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder(100);
        builder.append("ID = "+id);
        return builder.toString();
    }

    public Long getId() {
        return id;
    }

    public String getListingId() {
        return listingId;
    }

    public double getMrp() {
        return mrp;
    }

    public double getFsp() {
        return fsp;
    }

    public double getFk_discount() {
        return fk_discount;
    }

    public Timestamp getComputedAt() {
        return computedAt;
    }

    public String getJsonContext() {
        return jsonContext;
    }
}
