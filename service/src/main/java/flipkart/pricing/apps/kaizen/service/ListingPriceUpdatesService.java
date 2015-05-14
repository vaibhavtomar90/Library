package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 1:28 AM
 * Just a slim service/controller layer, before hitting the Datalayer
 */
public interface ListingPriceUpdatesService {
    PriceComputationResultSet getFromVersion(Long version, int numberOfRecords);
}
