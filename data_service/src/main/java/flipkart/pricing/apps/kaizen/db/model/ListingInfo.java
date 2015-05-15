package flipkart.pricing.apps.kaizen.db.model;


import flipkart.pricing.apps.kaizen.db.Model;

import javax.persistence.*;

@Entity
@Table
public class ListingInfo implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String listing;

    @Column(nullable = false)
    private long version;

    @Deprecated
    ListingInfo() {}

    public ListingInfo(String listing, long version) {
        this.listing = listing;
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public String getListing() {
        return listing;
    }

    public long getVersion() {
        return version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListingInfo)) return false;

        ListingInfo that = (ListingInfo) o;

        if (version != that.version) return false;
        if (listing != null ? !listing.equals(that.listing) : that.listing != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (listing != null ? listing.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ListingInfo{" +
                "id=" + id +
                ", listing='" + listing + '\'' +
                ", version=" + version +
                '}';
    }
}
