package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultset;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 05/05/15
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
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
    public Long savePriceComputeRecord(@NotNull PriceComputation priceComputation) {

        return this.productComputationRepository.savePriceComputation(priceComputation);
    }

    @Override
    public PriceComputation getPriceComputeRecordByVersion(Long version) {

        return this.productComputationRepository.getPriceComputation(version);
    }

    @Override
    public PriceComputationResultset getPriceComputation(Long fromComputePriceVersion, int countOfRecords) {

        final List<PriceComputation> priceComputations =
                this.productComputationRepository.getPriceComputation(fromComputePriceVersion, countOfRecords + 1);
        // send plus 1, to check, what is next version number and checking (in a single query), if there is more to read

        Boolean bMoreToRead = false;
        Long nextVersion = fromComputePriceVersion;

        if (priceComputations.size() > 0) {

            bMoreToRead = priceComputations.size() > countOfRecords;
            nextVersion = priceComputations.size() > countOfRecords ? priceComputations.get(countOfRecords + 1).getPriceVersion()
                    : priceComputations.get(priceComputations.size() - 1).getPriceVersion() + 1L;
        }

        return new PriceComputationResultset(priceComputations, nextVersion, bMoreToRead);
    }
}
