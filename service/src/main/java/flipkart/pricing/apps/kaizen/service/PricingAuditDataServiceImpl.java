package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:40 PM
 * Desc : DataServiceImpl, using a typical DataRepository of PricingComputation Objects
 */

@Service
@Transactional
public class PricingAuditDataServiceImpl implements PricingAuditDataService {

    private ProductComputationRepository productComputationRepository;

    @Inject
    public PricingAuditDataServiceImpl(ProductComputationRepository productComputationRepository) {
        this.productComputationRepository = productComputationRepository;
    }

    @Override
    public Long savePriceComputeRecord(@NotNull PriceComputeEvent priceComputation) {

        return this.productComputationRepository.savePriceComputation(priceComputation);
    }

    @Override
    public PriceComputation getPriceComputeRecordByVersion(Long version) {
        return this.productComputationRepository.getPriceComputation(version);
    }

    @Override
    public PriceComputationResultSet getPriceComputation(Long fromComputePriceVersion,
                                                            int countOfRecords)

    {

        final List<PriceComputation> priceComputations =
                this.productComputationRepository.getPriceComputation
                        (fromComputePriceVersion, countOfRecords + 1);
        // send plus 1, to check, what is next version number, if any, has to happen
        // a single transaction

        Long nextVersion = 0L;
        Boolean bMoreToRead = false;

        if (priceComputations.size() > 0) {

            nextVersion = fromComputePriceVersion;

            // means, there is more than can be read than, 'noOfCount' requested.
             bMoreToRead = priceComputations.size() > countOfRecords;

            if (bMoreToRead) {
                // read the next version, after requested 'countofRecords'
                nextVersion = priceComputations.get(priceComputations.size() - 1).getPriceVersion();
                priceComputations.remove(priceComputations.size() - 1); // remove the extra one!
            }
        }

        return new PriceComputationResultSet(priceComputations, nextVersion, bMoreToRead);
    }
}
