package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.exceptions.UnableToUpdateVersionException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ListingInfoDao extends AbstractDAO<ListingInfo> {

    private static final String INSERT_IGNORE_QUERY = "INSERT IGNORE INTO listing_infos (listing, version) values (:listing, 0)";
    private static final String UPDATE_VERSION_QUERY = "UPDATE listing_infos SET version = version + 1 WHERE listing = :listing";

    @Inject
    public ListingInfoDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ListingInfo insertIgnoreListing(String listing) {
        currentSession().createSQLQuery(INSERT_IGNORE_QUERY).setString("listing", listing).executeUpdate();
        currentSession().clear();
        Criteria criteria = currentSession().createCriteria(ListingInfo.class).add(Restrictions.eq("listing", listing));
        return uniqueResult(criteria);
    }


    public ListingInfo updateVersionAndGetListing(String listing) {
        int execUpdate = currentSession().createSQLQuery(UPDATE_VERSION_QUERY).setString("listing", listing).executeUpdate();
        if(execUpdate <= 0) {
            throw new UnableToUpdateVersionException(listing);
        }
        currentSession().clear();
        Criteria criteria = currentSession().createCriteria(ListingInfo.class).add(Restrictions.eq("listing", listing));
        return uniqueResult(criteria);
    }

    public ListingInfo fetchListingByNameWithWriteLock(String listing) {
        return fetchListingByName(listing, LockMode.PESSIMISTIC_WRITE);
    }

    public ListingInfo fetchListingByNameWithReadLock(String listing) {
        return fetchListingByName(listing, LockMode.PESSIMISTIC_READ);
    }

    private ListingInfo fetchListingByName(String listing, LockMode lockMode) {
        Criteria criteria = currentSession().createCriteria(ListingInfo.class);
        criteria.add(Restrictions.eq("listing", listing));
        criteria.setLockMode(lockMode);
        return uniqueResult(criteria);
    }

}
