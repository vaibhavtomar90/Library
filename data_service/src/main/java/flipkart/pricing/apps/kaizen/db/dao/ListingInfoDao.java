package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;

public class ListingInfoDao extends AbstractDAO<ListingInfo> {

    @Inject
    public ListingInfoDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public ListingInfo insertIgnoreListing(String listing) {
        currentSession().createSQLQuery("INSERT IGNORE INTO listing_infos (listing, version) values (:listing, 0)").setString("listing", listing).executeUpdate();
        //Won't this be too costly ??
        currentSession().clear();
        Criteria criteria = currentSession().createCriteria(ListingInfo.class).add(Restrictions.eq("listing", listing));
        return uniqueResult(criteria);
    }


    public ListingInfo updateVersionAndGetListing(ListingInfo listingInfo) { return listingInfo; }

    public ListingInfo fetchListingByName(String listing) { return null; }

    public ListingInfo fetchListingById(Long id) { return null; }

}
