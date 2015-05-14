package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:15 PM
 * Description : the Datalayer interface to read and write PricingAudit and Reconcililation objects.
 */
public interface PricingAuditDataService {

    Long savePriceComputeRecord(@NotNull PriceComputeEvent priceComputation);

    PriceComputation getPriceComputeRecordByVersion(Long version);

    PriceComputationResultSet getPriceComputation(Long fromComputePriceVersion, int countOfRecords);
}
