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
//@Table(name="PriceComputationAudit")
public class PriceComputationAudit implements Serializable{


    //@OneToOne(cascade = CascadeType.ALL, mappedBy = "priceComputationAudit", fetch = FetchType.LAZY)
    // @JoinColumn(name="listing_id",unique = true)
    // PriceComputationLatest reConAuditRecord;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "char", length = 26)
    String listingId;

    double mrp ;

    double fsp;

    double fk_discount ;

    Timestamp computedAt;

    @Column(columnDefinition = "text")
    String jsonContext;

    public PriceComputationAudit(String listingId, double mrp, double fsp, double fk_discount, String jsonContext) {
        this.listingId = listingId;
        this.mrp = mrp;
        this.fsp = fsp;
        this.fk_discount = fk_discount;
        this.jsonContext = jsonContext;
    }

    @Deprecated
    PriceComputationAudit() {

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
