package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultset;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface PricingAuditDataService {



    Long savePriceComputeRecord(@NotNull PriceComputation priceComputation);

    PriceComputation getPriceComputeRecordByVersion(Long version);

    PriceComputationResultset getPriceComputation(Long fromComputePriceVersion, int countOfRecords);




}
