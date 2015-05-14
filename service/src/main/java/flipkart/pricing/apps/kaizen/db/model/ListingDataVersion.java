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
public class ListingDataVersion implements Serializable{

    @Id
    @Column(nullable = false, columnDefinition = "char", length = 26)
    private String listingID;

    private Long dataVersion;

    public ListingDataVersion(@NotNull String listingID,
                              @NotNull Long listingDataVersion) {
        this.listingID = listingID;
        this.dataVersion = listingDataVersion;
    }

    public String getListingID() {
        return listingID;
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Deprecated
    protected ListingDataVersion(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListingDataVersion that = (ListingDataVersion) o;

        if (!dataVersion.equals(that.dataVersion)) return false;
        if (!listingID.equals(that.listingID)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = listingID.hashCode();
        result = 31 * result + dataVersion.hashCode();
        return result;
    }
}
