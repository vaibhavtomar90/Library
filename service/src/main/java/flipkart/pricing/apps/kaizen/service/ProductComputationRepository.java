package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 12/05/15
 * Time: 11:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductComputationRepository {
     Long savePriceComputation(PriceComputation priceComputation) ;
     PriceComputation getPriceComputation(Long computePriceVersion);
     List<PriceComputation> getPriceComputation(Long fromComputePriceVersion, int countOfRecords);
}
