package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.db.dao.ListingDataVersionDAO;
import flipkart.pricing.apps.kaizen.db.dao.PriceComputationAuditDAO;
import flipkart.pricing.apps.kaizen.db.dao.PriceComputationLatestDAO;
import flipkart.pricing.apps.kaizen.db.model.ListingDataVersion;
import flipkart.pricing.apps.kaizen.db.model.PriceComputationAudit;
import flipkart.pricing.apps.kaizen.db.model.PriceComputationLatest;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;
import flipkart.pricing.apps.kaizen.service.datatypes.PricingData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 11/05/15
 * Time: 11:23 PM
 * DB Based (PriceComputationAudit and PriceComputationLatest tables!)
 */

@Service
@Transactional
@Component
public class ProductComputationRepositoryImpl implements  ProductComputationRepository {

    private static Logger logger = LoggerFactory.getLogger(ProductComputationRepositoryImpl.class);

    // TODO :Right now, confining to concrete class,
    // better would been just another DAO Interface over
    // DRopwizard's AbstractDAO<>

    PriceComputationAuditDAO priceComputationAuditDAO;

    PriceComputationLatestDAO priceComputationLatestDAO;

    ListingDataVersionDAO listingDataVersionDAO;

    @Inject
    public ProductComputationRepositoryImpl(
            PriceComputationAuditDAO priceComputationAuditDAO,
            PriceComputationLatestDAO priceComputationLatestDAO,
            ListingDataVersionDAO listingDataVersionDAO)
    {
        this.priceComputationAuditDAO = priceComputationAuditDAO;
        this.priceComputationLatestDAO = priceComputationLatestDAO;
        this.listingDataVersionDAO = listingDataVersionDAO;
    }


    // To be called within a transaction
    public Long savePriceComputation(PriceComputeEvent priceComputeEvent) {

        Long priceChangeId = 0L;
        try {

            do {
                ListingDataVersion listingDataVersion = new ListingDataVersion(priceComputeEvent.getListingID(),
                        priceComputeEvent.getDataVersion());

                // Algo : We take write pessimistic lock on ListingDataVersion, to block the
                // concurrent update on a ListingID in question. If MVCC is kicks in we return
                // from there, without processing the event further.
                // We have BIG lock (per listing level) via 'ListingDataVersion' table. EVerything is atomic on
                // per listing basis.So, no need
                // to take exclusive locks subsequently/at-least on per Listing level

                if (!listingDataVersionDAO.updateListing(listingDataVersion))
                    break;

                // else,we have work to do
                String listingID = priceComputeEvent.getListingID();

                // First check, if there is any existing PriceComputationLatest entry exists for given 'listingID'
                // if yes, we are going to update it, by this newer PriceComputeEvent link

                PriceComputationLatest priceComputationLatest = priceComputationLatestDAO.get(listingID);

                if (priceComputationLatest == null)
                    priceComputationLatest = new PriceComputationLatest(listingID);


                PricingData pricingData = priceComputeEvent.getPricingData();
                PriceComputationAudit priceComputationAudit = new PriceComputationAudit(listingID, pricingData.getMrp(),
                        pricingData.getFsp(), pricingData.getFk_discount(), priceComputeEvent.getJsonContext());


                // get the identity values generated , which you wont get, unless call  save
                // on Hibernate session.
                // this is required to set all the hibernate graph of object in a sane state

                priceChangeId = priceComputationAuditDAO.save(priceComputationAudit);

                // patch this Audit Row, with 'PriceComputationLatest' as a foreign key
                // this is where the generated 'priceChangeID' gets linked up
                priceComputationLatest.setPriceComputationAudit(priceComputationAudit);

                priceComputationLatestDAO.upsert(priceComputationLatest);

                // we have two rows at the end of this

                // 1. Global PriceComputationLatest  table (1 update or new)
                // 2. Price Audit log table  (1 new)

            } while (false);


        } catch (Exception ex) {

            String msg = "Error in savePriceComputation for ListingID=" + priceComputeEvent.getListingID();
            logger.error(msg, ex);
            throw ex;

        } finally {

        }

        return priceChangeId;
    }

    public PriceComputation getPriceComputation(Long computePriceVersion) {
        PriceComputationLatest priceComputationLatest =
                this.priceComputationLatestDAO.getByVersion(computePriceVersion);
        return map(priceComputationLatest);
    }

    public List<PriceComputation> getPriceComputation(Long fromComputePriceVersion, int countOfRecords) {

        final List<PriceComputationLatest> reConRecords =
                priceComputationLatestDAO.getReConRecords(fromComputePriceVersion, countOfRecords);

        ArrayList<PriceComputation> priceComputations = new ArrayList<>(reConRecords.size());
        for (PriceComputationLatest priceComputationLatest : reConRecords)
            priceComputations.add(map(priceComputationLatest));

        return priceComputations;
    }

    // Converting the underlying DB Model objects to domain level objects
    private PriceComputation map(PriceComputationLatest priceComputationLatest) {

        PriceComputation priceComputation = null;

        if (priceComputationLatest != null) {
            PriceComputationAudit auditRecord = priceComputationLatest.getPriceComputationAudit();
            priceComputation = new
                    PriceComputation(auditRecord.getListingId(),auditRecord.getId(),
                    new PricingData(auditRecord.getFsp(),auditRecord.getMrp(),auditRecord.getFk_discount()),
                    auditRecord.getComputedAt(),
                    auditRecord.getJsonContext());

        }
        return priceComputation;
    }
}
