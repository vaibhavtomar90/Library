package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.model.ListingDataVersion;
import flipkart.pricing.apps.kaizen.testrules.KaizenDBClearRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
public class ListingDataVersionDAOIntegrationTest {

    @Rule
    @Inject
    public KaizenDBClearRule kaizenDBClearRule;

    @Inject
    ListingDataVersionDAO listingDataVersionDAO;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testUpdateListingForNonExistentVersion() throws Exception {

        String listingId = "LST123544";
        ListingDataVersion listingDataVersion = new ListingDataVersion(listingId, 100L) ;
        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);
        ListingDataVersion dataVersion =  listingDataVersionDAO.get(listingId);
        assertTrue(dataVersion.getListingID().equals(listingId) &&
                dataVersion.getDataVersion().equals(new Long(100L)));
    }

    @Test
    @Transactional
    @Rollback(false)

    public void testUpdateListingForExistentVersionWithHigherVersion() throws Exception {

        String listingId = "LST123545";
        ListingDataVersion listingDataVersion = new ListingDataVersion(listingId, 100L) ;

        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);

        assertTrue(listingDataVersionDAO.updateListing(new ListingDataVersion(listingId, 99L)) == false);

        ListingDataVersion dataVersion =  listingDataVersionDAO.get(listingId);
        assertTrue(dataVersion.getListingID().equals(listingId) &&
                dataVersion.getDataVersion().equals(new Long(100L)));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testUpdateListingForExistentVersionWithLowerVersion() throws Exception {

        ListingDataVersion listingDataVersion = new ListingDataVersion("LST123544", 99L) ;
        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);
        assertTrue(listingDataVersionDAO.updateListing(new ListingDataVersion("LST123544", 100L)) == true);
    }
}
