package flipkart.pricing.apps.kaizen.service;

import flipkart.pricing.apps.kaizen.db.dao.ListingDataVersionDAO;
import flipkart.pricing.apps.kaizen.db.dao.PricingAuditRecordDAO;
import flipkart.pricing.apps.kaizen.db.dao.PriceReConComputationDAO;
import flipkart.pricing.apps.kaizen.db.model.ListingDataVersion;
import flipkart.pricing.apps.kaizen.db.model.PricingAuditRecord;
import flipkart.pricing.apps.kaizen.db.model.ReConAuditRecord;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * To change this template use File | Settings | File Templates.
 */

@Service
@Transactional
@Component
public class ProductComputationRepositoryImpl implements  ProductComputationRepository {

    private static Logger logger = LoggerFactory.getLogger(ProductComputationRepositoryImpl.class);


    @Inject
    PricingAuditRecordDAO priceAuditDao;

    @Inject
    PriceReConComputationDAO priceReConLogDao;

    @Inject
    ListingDataVersionDAO listingDataVersionDAO;

    public Long savePriceComputation(PriceComputation priceComputation) {

        Long priceChangeId = 0L;

        try {

            do {

                ListingDataVersion listingDataVersion = new ListingDataVersion(priceComputation.getListingID(),
                        priceComputation.getPriceVersion());

                // BEGIN TRANS


                // MVCC kicked in, new version already existed, dropping it here
                if (!listingDataVersionDAO.updateListing(listingDataVersion))
                    break;

                // else,we have work to do


                String listingID = priceComputation.getListingID();

                // check, if there is any existing ReCon entry exists for given 'listingID'
                // if yes, we are going to simply update it


                ReConAuditRecord reConAuditRecord = priceReConLogDao.get(listingID);

                //  this is for making sure, if the listing ID never got updated and
                // we are getting it for the firs time.
                // We have BIG lock (per listing level) on top of this, so we are guaranteed that,
                // the existence check and setting of the listingID a fresh is atomic
                if (reConAuditRecord == null)
                    reConAuditRecord = new ReConAuditRecord(listingID);


                PricingAuditRecord pricingAuditRecord = new PricingAuditRecord(listingID, priceComputation.getMrp(),
                        priceComputation.getFsp(), priceComputation.getFk_discount());


                // get the identity values generated , which you wont get, unless you save
                // this is required to set all the hibernate graph of object in a sane state

                priceChangeId = priceAuditDao.save(pricingAuditRecord);


                // patch this Audit Row, with ReCon as a foreign key
                // this is where the generated 'priceChangeID' gets linked up
                reConAuditRecord.setPricingAuditRecord(pricingAuditRecord);

                priceReConLogDao.upsert(reConAuditRecord);

                // we have two rows at the end of this


                // END
            } while (false);


        } catch (Exception ex) {

            logger.error("Error in savePriceComputation", ex);
            throw ex;
            // logging to be added

        } finally {

        }

        return priceChangeId;
    }

    public PriceComputation getPriceComputation(Long computePriceVersion) {
        ReConAuditRecord reConAuditRecord = this.priceReConLogDao.getByVersion(computePriceVersion);
        return map(reConAuditRecord);
    }

    public List<PriceComputation> getPriceComputation(Long fromComputePriceVersion, int countOfRecords) {

        final List<ReConAuditRecord> reConRecords =
                priceReConLogDao.getReConRecords(fromComputePriceVersion, countOfRecords);

        ArrayList<PriceComputation> priceComputations = new ArrayList<>(reConRecords.size());
        for (ReConAuditRecord reConAuditRecord : reConRecords)
            priceComputations.add(map(reConAuditRecord));

        return priceComputations;
    }

    // Converting the lying Model objects to domain level objects
    private PriceComputation map(ReConAuditRecord reConAuditRecord) {

        PriceComputation priceComputation = null;

        if (reConAuditRecord != null) {
            PricingAuditRecord auditRecord = reConAuditRecord.getPricingAuditRecord();
            priceComputation = new
                    PriceComputation(reConAuditRecord.getListingId(),
                    auditRecord.getId(), auditRecord.getMrp(), auditRecord.getFsp(), auditRecord.getFk_discount(),
                    auditRecord.getJsonContext());

        }
        return priceComputation;
    }
}
