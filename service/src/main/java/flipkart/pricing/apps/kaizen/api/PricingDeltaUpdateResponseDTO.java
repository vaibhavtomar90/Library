package flipkart.pricing.apps.kaizen.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:43 AM
 * Description : DTO For Delta API response
 */

public class PricingDeltaUpdateResponseDTO implements Serializable{

    @JsonProperty
    @NotNull
    final List<PricingUpdate> updates;

    @JsonProperty
    @NotNull
    final Long nextVersion;

    @JsonProperty
    @NotNull
    final Boolean moreData;

    @JsonCreator
    public PricingDeltaUpdateResponseDTO(@JsonProperty("updates") List<PricingUpdate> updates,
                                         @JsonProperty("nextVersion") Long nextVersion,
                                         @JsonProperty("moreData")Boolean moreData){
        this.moreData = moreData;
        this.nextVersion = nextVersion;
        this.updates = updates;
    }

    public List<PricingUpdate> getUpdates() {
        return updates;
    }

    public Long getNextVersion() {
        return nextVersion;
    }

    public Boolean getMoreData() {
        return moreData;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(1024);
        stringBuilder.append(getClass().getSimpleName()).append("[").append("moreData=[").append(getMoreData()).append("]")
                .append("nextVersion=").append(nextVersion).append("[updates=[").append(this.updates.toString()).append("]]");
        return stringBuilder.toString();
    }
}
