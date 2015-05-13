package flipkart.pricing.apps.kaizen.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 11/05/15
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="ListingDataVersion")
public class ListingDataVersion implements Serializable{


    @Id
    @Column(nullable = false, columnDefinition = "char", length = 26)
    private String listingID;

    @Column(name="version", nullable = false)
    private Long listingDataVersion;

    public ListingDataVersion(@NotNull String listingID, @NotNull Long listingDataVersion) {
        this.listingID = listingID;
        this.listingDataVersion = listingDataVersion;
    }

    public String getListingID() {
        return listingID;
    }

    public Long getListingDataVersion() {
        return listingDataVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListingDataVersion that = (ListingDataVersion) o;

        if (!listingDataVersion.equals(that.listingDataVersion)) return false;
        if (!listingID.equals(that.listingID)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingID.hashCode();
        result = 31 * result + listingDataVersion.hashCode();
        return result;
    }

    @Deprecated
    protected ListingDataVersion(){

    }

    public void setListingDataVersion(Long listingDataVersion) {
        this.listingDataVersion = listingDataVersion;
    }
}
