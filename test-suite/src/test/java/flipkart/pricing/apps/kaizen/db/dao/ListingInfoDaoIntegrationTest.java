package flipkart.pricing.apps.kaizen.db.dao;


import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.exceptions.UnableToUpdateVersionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KaizenContextConfiguration.class)
@ActiveProfiles("test")
public class ListingInfoDaoIntegrationTest {

    @Inject
    private ListingInfoDao listingInfoDao;


    @Test
    @Transactional
    public void shouldInsertListingIfNotPresent() {
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        assertNotNull(listingInfo.getId());
        assertTrue(listingInfo.getListing().compareTo("foo") == 0);
        assertTrue(listingInfo.getVersion() == 0l);
    }

    @Test
    @Transactional
    public void shouldNotInsertListingIfAlreadyPresent() {
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        ListingInfo listingInfo1 = listingInfoDao.insertIgnoreListing("foo");
        assertTrue(listingInfo.equals(listingInfo1));
    }

    @Test
    @Transactional
    public void shouldIncrementVersionIfListingIsPresent() {
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing("foo");
        ListingInfo listingInfo1 = listingInfoDao.updateVersionAndGetListing("foo");
        assertTrue(listingInfo1.getVersion() == listingInfo.getVersion() + 1l);
    }

    @Test(expected = UnableToUpdateVersionException.class)
    @Transactional
    public void shouldThrowExceptionForUpdateVersionIfListingIsNotPresent() {
        listingInfoDao.updateVersionAndGetListing("foo");
    }

    //TODO test read and write locks
    @Test
    @Transactional
    public void shouldFetchListingInfo() {
        ListingInfo persistedListingInfo = listingInfoDao.insertIgnoreListing("foo");
        assertTrue(listingInfoDao.fetchListingByNameWithReadLock("foo").equals(persistedListingInfo));
        assertTrue(listingInfoDao.fetchListingByNameWithWriteLock("foo").equals(persistedListingInfo));
    }





}
