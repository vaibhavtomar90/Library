package flipkart.pricing.apps.kaizen.db.service;

import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.service.PricingAuditDataService;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputation;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputationResultSet;
import flipkart.pricing.apps.kaizen.service.datatypes.PriceComputeEvent;
import flipkart.pricing.apps.kaizen.service.datatypes.PricingData;
import flipkart.pricing.apps.kaizen.testrules.KaizenDBClearRule;
import flipkart.pricing.apps.kaizen.utils.ListingLatestPriceVersion;
import flipkart.pricing.apps.kaizen.utils.PricingAuditEventUtils;
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
import java.util.ArrayList;

import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 13/05/15
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})

public class PricingAuditDataServiceIntegrationTest {

    @Inject
    PricingAuditDataService pricingAuditDataService;

    @Inject
    PricingAuditEventUtils pricingAuditEventUtils;

    @Inject
    @Rule
    public KaizenDBClearRule kaizenDBClearRule;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testSavePriceComputeAndGetPriceComputation() throws Exception {

        PricingData pricingData = new PricingData(100.0, 150.0, 50.0);

        PriceComputeEvent priceComputationEvent =
                new PriceComputeEvent("LISTINGABRADABRA", 100L, pricingData, "TEXT");

        final Long priceVersion =
                this.pricingAuditDataService.savePriceComputeRecord(priceComputationEvent);

        assertTrue(priceVersion > 0L);

        final PriceComputation priceComputation =
                this.pricingAuditDataService.getPriceComputeRecordByVersion(priceVersion);

        assertTrue(priceComputation.getPricingData().equals(pricingData));
        assertTrue(priceComputation.getListingID().equals("LISTINGABRADABRA"));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testComputeStoreAndTryWithLowerVersion() throws Exception {

        PricingData pricingData = new PricingData(100.0, 150.0, 50.0);

        PriceComputeEvent priceComputationEvent =
                new PriceComputeEvent("LISTINGABRADABRA", 100L, pricingData, "TEXT");

        final Long priceVersion =
                this.pricingAuditDataService.savePriceComputeRecord(priceComputationEvent);

        assertTrue(priceVersion > 0L);

        final PriceComputation priceComputation =
                this.pricingAuditDataService.getPriceComputeRecordByVersion(priceVersion);

        final Long priceVersion2 = this.pricingAuditDataService.savePriceComputeRecord(
                new PriceComputeEvent("LISTINGABRADABRA", 90L, pricingData, "TEXT"));

        assertTrue(priceVersion2 == 0L); // means, it doesnt impact price even if, lower version comes in later!

        final PriceComputation priceComputationAfterSave =
                this.pricingAuditDataService.getPriceComputeRecordByVersion(priceVersion);

        assertTrue(priceComputation.equals(priceComputationAfterSave));
    }


    // This depicts the testcase, to ensure, the Events are inserted corrected
    // and calls getReConcileRecord api to read them back from DB, with right version numbers

    @Test
    @Transactional
    @Rollback(false)
    public void testGenerateNComputesAndGetAllNUpdatesViaDeltaAPI() {

        ArrayList<ListingLatestPriceVersion> arrayList = pricingAuditEventUtils.generateComputationRecords("LISTCAMERA", 20, 2);

        int requestedCount = arrayList.size();

        final PriceComputationResultSet priceComputationResultSet =
                this.pricingAuditDataService.getPriceComputation(0L, requestedCount);

        assertTrue(priceComputationResultSet.getPriceComputations().size() == requestedCount);

        int idx = 0;
        for (PriceComputation pc : priceComputationResultSet.getPriceComputations()) {
            assertTrue(pc.getListingID().equals("LISTCAMERA" + new Integer(idx).toString()));
            assertTrue(pc.getPriceVersion().longValue() == arrayList.get(idx).getLatestPriceVersion().longValue());
            ++idx;
        }

        assertTrue(priceComputationResultSet.getHasMoreToRead() == false);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testNComputesAndGetLessThanNUpdatesViaDeltaAPI() {

        ArrayList<ListingLatestPriceVersion> arrayList =
                pricingAuditEventUtils.generateComputationRecords("LISTINGABRACADABRA", 20, 2);

        int requestedCount = arrayList.size();
        --requestedCount;

        final PriceComputationResultSet priceComputationResultSet =
                this.pricingAuditDataService.getPriceComputation(0L, requestedCount);

        assertTrue(priceComputationResultSet.getPriceComputations().size() == requestedCount);

        int idx = 0;

        for (PriceComputation pc : priceComputationResultSet.getPriceComputations()) {
            assertTrue(pc.getListingID().equals("LISTINGABRACADABRA" + new Integer(idx).toString()));
            assertTrue(pc.getPriceVersion().longValue() == arrayList.get(idx).getLatestPriceVersion().longValue());
            ++idx;
        }

        assertTrue(priceComputationResultSet.getHasMoreToRead() == true);
        assertTrue(priceComputationResultSet.getNextAvailableVersion().longValue()
                == arrayList.get(requestedCount).getLatestPriceVersion().longValue());
    }
}
