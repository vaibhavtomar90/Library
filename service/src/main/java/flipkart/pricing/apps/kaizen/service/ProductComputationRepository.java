package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 12/05/15
 * Time: 11:47 PM
 * Description : Repository interface to storing the PriceComputation Objects
 */
public interface ProductComputationRepository {
     Long savePriceComputation(PriceComputeEvent priceComputation) ;
     PriceComputation getPriceComputation(Long computePriceVersion);
     List<PriceComputation> getPriceComputation(Long fromComputePriceVersion,
                                                int countOfRecords);
}
