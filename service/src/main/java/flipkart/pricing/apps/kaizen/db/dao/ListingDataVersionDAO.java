package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.db.model.ListingDataVersion;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 11/05/15
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
@Repository
public class ListingDataVersionDAO extends AbstractDAO<ListingDataVersion> {

    private static Logger logger = LoggerFactory.getLogger(ListingDataVersionDAO.class);

    private static final String INSERT_IGNORE =
            "INSERT IGNORE INTO ListingDataVersion (listingID, dataVersion) values (:listingID, :version)";

    private static final String UPDATE_LISTINGDATA =
            "UPDATE ListingDataVersion " +
                    " set dataVersion = :version where (listingID = :listingID AND dataVersion < :version )";

    @Inject
    public ListingDataVersionDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * "Upserts' the listing dataset version in ListingDataVersion table
     * which , essentially acts as a MVCC table, to avoid concurrent listing level
     * updates. To be called via Transaction, with combination with other
     * PriceComputation Audit Table, thru high layers
     *
     * @param listingDataVersion
     * @return listDataVersion, of the upserted ListingDataVersion record
     */

    public Boolean updateListing(ListingDataVersion listingDataVersion) {

        Boolean isListingUpdated = false;

        do {
            // 1. Upsert ListingDataVersion Record for listing 'L'

            String listingID = listingDataVersion.getListingID();
            Long listingVersion = listingDataVersion.getDataVersion();

            int noOfUpdatedRecords = currentSession().createSQLQuery(
                    INSERT_IGNORE).
                    setString("listingID", listingID).
                    setLong("version", listingVersion).executeUpdate();

            if (noOfUpdatedRecords > 0) {
                isListingUpdated = Boolean.TRUE;
                logger.info("Fresh Listing came in {0}", listingID);
                break;
            }

            Criteria criteria = currentSession().createCriteria(ListingDataVersion.class).
                    add(Restrictions.eq("listingID", listingID)).
                    setLockMode(LockMode.PESSIMISTIC_WRITE);// paranoia?? take force exclusive lock on the row LISTINGID

            ListingDataVersion dataVersion = uniqueResult(criteria);

            if (dataVersion == null) {
                // this really cant happen
                throw new RuntimeException("Could not insert or retrieve ListingDataVersion = " + listingID);
            }

            // Nasty hibernate, was caching the data, and writing back the older data
            // before terminating the session..thereby overwriting the useful updates,
            // done below . Probably I dnt understand Hibernate behavior well..
            // but it works!! in all cases
            currentSession().evict(dataVersion);

            dataVersion.setDataVersion(listingVersion);

            // now lets do the version bump. We are sure here, that  ListingID is present in DB
            noOfUpdatedRecords = testAndIncrementListingVersion(dataVersion);
            // noOfUpdateRecords == 0, then either the existing version in the DB, was greater than the
            //  listingDataVersion, else we pass the updated row count, which should be always 1

            if (noOfUpdatedRecords > 0) {
                isListingUpdated = Boolean.TRUE;
                break;
            }
        } while (false);

        return isListingUpdated;
    }

    // only for test
    public ListingDataVersion get(String id) {
        return super.get(id);
    }

    private int testAndIncrementListingVersion(ListingDataVersion listingDataVersion) {

        String listingID = listingDataVersion.getListingID();

        int executeUpdate = currentSession().createSQLQuery(UPDATE_LISTINGDATA).
                setString("listingID", listingID).
                setLong("version", listingDataVersion.getDataVersion()).
                executeUpdate();

        if (executeUpdate == 0)
            logger.info("Ignoring the Version {0} for {1} as MVCC kicks in", listingDataVersion.getDataVersion(),
                    listingID);

        return executeUpdate;
    }
}
