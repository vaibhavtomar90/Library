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
            "INSERT IGNORE INTO ListingDataVersion (listingID, version) values (:listingID, :version)";

    private static final String UPDATE_LISTINGDATA =
            "UPDATE ListingDataVersion" +
                    " set version = :new_version where listingID = :listingID AND :new_version > version";

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
            Long listingVersion = listingDataVersion.getListingDataVersion();

            int noOfUpdatedRecords = currentSession().createSQLQuery(
                    INSERT_IGNORE).
                    setString("listingID", listingID).
                    setLong("version", listingVersion).executeUpdate();

            if (noOfUpdatedRecords > 0) {
                isListingUpdated = Boolean.TRUE;
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

            dataVersion.setListingDataVersion(listingVersion);

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

    private int testAndIncrementListingVersion(ListingDataVersion listingDataVersion) {

        String listingID = listingDataVersion.getListingID();

        int executeUpdate = currentSession().createSQLQuery(UPDATE_LISTINGDATA).
                setString("listingID", listingID).
                setLong("new_version", listingDataVersion.getListingDataVersion()).
                executeUpdate();

        currentSession().flush();

        return executeUpdate;
    }
}
