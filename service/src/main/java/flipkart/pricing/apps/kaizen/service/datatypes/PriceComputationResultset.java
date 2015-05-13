package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriceComputationResultset implements Serializable{
    final List<PriceComputation> priceComputations;
    final Long nextAvailableVersion;
    final Boolean hasMoreToRead;

    public PriceComputationResultset(List<PriceComputation> priceComputations, Long nextAvailableVersion, Boolean hasMoreToRead) {
        this.priceComputations = priceComputations;
        this.nextAvailableVersion = nextAvailableVersion;
        this.hasMoreToRead = hasMoreToRead;
    }

    public Boolean getHasMoreToRead() {
        return hasMoreToRead;
    }

    public List<PriceComputation> getPriceComputations() {
        return priceComputations;
    }

    public Long getNextAvailableVersion() {
        return nextAvailableVersion;
    }
}