package flipkart.pricing.apps.kaizen.api;

import java.util.Objects;

/**
 * @understands Message that goes out once new listing data is saved
 */
public class ListingUpdateMessage {
    private final String listingId;

    public ListingUpdateMessage(String listingId) {
        this.listingId = listingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListingUpdateMessage that = (ListingUpdateMessage) o;
        return Objects.equals(listingId, that.listingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listingId);
    }

    @Override
    public String toString() {
        return "ListingUpdateMessage{" + "listingId='" + listingId + '\'' + '}';
    }
}
