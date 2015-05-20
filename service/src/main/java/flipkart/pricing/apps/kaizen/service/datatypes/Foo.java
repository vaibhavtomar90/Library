package flipkart.pricing.apps.kaizen.service.datatypes;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:41 AM
 * Description : High level ResultSet sort of class, to present the results
 * received by Delta API
 */
public class PriceComputationResultSet implements Serializable{

    /**
     * List of PriceComputations, usually returned by the Delta API
     */
    final List<PriceComputation> priceComputations;
    /**
     * Self explanatory. If nothing to be read further, then 0.
     */
    final Long nextAvailableVersion;

    final Boolean hasMoreToRead;

    public PriceComputationResultSet(List<PriceComputation> priceComputations,
                                     Long nextAvailableVersion,
                                     Boolean hasMoreToRead) {
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