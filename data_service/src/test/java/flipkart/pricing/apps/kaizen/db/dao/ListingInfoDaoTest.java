package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.exceptions.UnableToUpdateVersionException;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRule;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class ListingInfoDaoTest {

    @Rule
    public HibernateSessionTestRule hibernateSessionTestRule = new HibernateSessionTestRule();

    @Test
    public void shouldInsertListingIfNotPresent() {
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        assertNotNull(listingInfo.getId());
        assertTrue(listingInfo.getListing().compareTo("foo") == 0);
        assertTrue(listingInfo.getVersion() == 0l);
    }

    @Test
    public void shouldNotInsertListingIfAlreadyPresent() {
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        ListingInfo listingInfo1 = listingInfoDao.insertIgnoreListing("foo");
        assertTrue(listingInfo.equals(listingInfo1));
    }

    @Test
    public void shouldIncrementVersionIfListingIsPresent() {
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        ListingInfo listingInfo1 = listingInfoDao.updateVersionAndGetListing("foo");
        assertTrue(listingInfo1.getVersion() == listingInfo.getVersion() + 1l);
    }

    @Test(expected = UnableToUpdateVersionException.class)
    public void shouldThrowExceptionForUpdateVersionIfListingIsNotPresent() {
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        listingInfoDao.updateVersionAndGetListing("foo");
    }

    @Test
    //TODO test read and write locks
    public void shouldFetchListingInfo() {
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfo persistedListingInfo = listingInfoDao.insertIgnoreListing("foo");
        assertTrue(listingInfoDao.fetchListingByNameWithReadLock("foo").equals(persistedListingInfo));
        assertTrue(listingInfoDao.fetchListingByNameWithWriteLock("foo").equals(persistedListingInfo));
    }





}
