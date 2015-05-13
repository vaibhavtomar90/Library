package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultset;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:28 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ListingPriceUpdatesService {

    PriceComputationResultset getFromVersion(Long version, int numberOfRecords);

}
