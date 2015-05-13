package flipkart.pricing.apps.kaizen.db.dao;

import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.model.ListingDataVersion;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRulePC;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;


/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})

public class ListingDataVersionDAOTest {

    @Rule
    @Inject
    public HibernateSessionTestRulePC hibernateSessionTestRulePC;

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
    public void testUpdateListingForNonExistentVersion() throws Exception {

        ListingDataVersion listingDataVersion = new ListingDataVersion("LST123544", 100L) ;

        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);
        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);

    }

    @Test
    @Transactional
    public void testUpdateListingForExistentVersionWithHigherVersion() throws Exception {

        ListingDataVersion listingDataVersion = new ListingDataVersion("LST123544", 100L) ;

        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);

        hibernateSessionTestRulePC.getSessionFactory().getCurrentSession().flush();;

        assertTrue(listingDataVersionDAO.updateListing(new ListingDataVersion("LST123544", 99L)) == false);

    }

    @Test
    @Transactional
    public void testUpdateListingForExistentVersionWithLowerVersion() throws Exception {


        ListingDataVersion listingDataVersion = new ListingDataVersion("LST123544", 99L) ;

        assertTrue(listingDataVersionDAO.updateListing(listingDataVersion) == true);

        hibernateSessionTestRulePC.getSessionFactory().getCurrentSession().flush();;

        assertTrue(listingDataVersionDAO.updateListing(new ListingDataVersion("LST123544", 100L)) == true);

    }
}
