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
    final Long next_version;

    @JsonProperty
    @NotNull
    final Boolean more_data;

    @JsonCreator
    public PricingDeltaUpdateResponseDTO(@JsonProperty("updates") List<PricingUpdate> updates,
                                         @JsonProperty("next_version") Long next_version,
                                         @JsonProperty("more_data")Boolean more_data){
        this.more_data = more_data;
        this.next_version = next_version;
        this.updates = updates;
    }

    public List<PricingUpdate> getUpdates() {
        return updates;
    }

    public Long getNext_version() {
        return next_version;
    }

    public Boolean getMore_data() {
        return more_data;
    }
}
