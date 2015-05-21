package flipkart.pricing.apps.kaizen.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import flipkart.pricing.apps.kaizen.api.PricingDeltaUpdateResponseDTO;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.testrules.KaizenDBClearRule;
import flipkart.pricing.apps.kaizen.testrules.SpringAwareResourceTestRule;
import flipkart.pricing.apps.kaizen.utils.ListingLatestPriceVersion;
import flipkart.pricing.apps.kaizen.utils.PricingAuditEventUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: bhushan.sk
 * Date: 14/05/15
 * Time: 8:04 AM
 * Integration test to test the Delta API (fromVersion and count) REST endpoint,
 * Audit DB Repository and underlying DAOs
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
public class ListingPriceUpdatesResourceIntegrationTest {
    @Rule
    @Inject
    public SpringAwareResourceTestRule resources;

    @Rule
    @Inject
    public KaizenDBClearRule dbClearRule;

    @Inject
    ListingPriceUpdatesResource listingPriceUpdatesResource;

    @Inject
    PricingAuditEventUtils pricingAuditEventUtils;

    @Before
    public void setUp() throws Exception {

    }

    @Transactional
    @Rollback(false)
    @Test
    public void testWritePriceComputesAndRetrieveViaRestDeltaAPI() throws Exception {

        final int countOfGeneratedListing = 10;
        final int countOfListingUpdates = 1;

        final ArrayList<ListingLatestPriceVersion> latestPriceVersions =
                pricingAuditEventUtils.generateComputationRecords("LSTBOOKSAVMEDIA",
                        countOfGeneratedListing, countOfListingUpdates);

        assertTrue(latestPriceVersions.size() == countOfGeneratedListing);

        final Response response = resources.client().
                target("/v1/listingPriceUpdate/delta").queryParam("version", 0L).queryParam("count", 10).request().
                get();

        String jsonResponse = IOUtils.toString((InputStream) response.getEntity());

        PricingDeltaUpdateResponseDTO priceUpdateDTO =
                new ObjectMapper().readValue(jsonResponse, PricingDeltaUpdateResponseDTO.class);

        assertTrue(priceUpdateDTO.getUpdates().size() == countOfGeneratedListing);

        assertTrue(priceUpdateDTO.getMoreData() == false); // as we have asked for all versions

        assertTrue(priceUpdateDTO.getNextVersion() == 0L); // since, there is nothing to read further

        // check, if the JSON fields match with all the generated ones.
        for (int i = 0; i < countOfGeneratedListing; ++i) {

            assertTrue(priceUpdateDTO.getUpdates().get(i).getVersion().equals(
                    latestPriceVersions.get(i).getLatestPriceVersion()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getListing().equals(latestPriceVersions.get(i).getListingId()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getFk_discount().equals(
                    latestPriceVersions.get(i).getPricingData().getFk_discount()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getMrp().equals(
                    latestPriceVersions.get(i).getPricingData().getMrp()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getFsp().equals(
                    latestPriceVersions.get(i).getPricingData().getFsp()));
        }
    }

    @Transactional
    @Rollback(false)
    @Test
    public void testWritePriceComputesAndRetrievePartialEntriesViaRestDeltaAPI() throws Exception {

        final int countOfGeneratedListing = 10;
        final int countRecords = 5;
        final int countOfListingUpdates = 2;


        final ArrayList<ListingLatestPriceVersion> latestPriceVersions =
                pricingAuditEventUtils.generateComputationRecords("LSTAVMEDIA",
                        countOfGeneratedListing, countOfListingUpdates);

        assertTrue(latestPriceVersions.size() == countOfGeneratedListing);

        final Response response = resources.client().
                target("/v1/listingPriceUpdate/delta").queryParam("version", 0L).queryParam("count", countRecords).request().
                get();

        String jsonResponse = IOUtils.toString((InputStream) response.getEntity());
        PricingDeltaUpdateResponseDTO priceUpdateDTO =
                new ObjectMapper().readValue(jsonResponse, PricingDeltaUpdateResponseDTO.class);

        assertTrue(priceUpdateDTO.getUpdates().size() == countRecords);

        assertTrue(priceUpdateDTO.getMoreData() == true); // as we have asked for all 5 versions out of 10 generated

        // check, if the JSON fields match with all the generated ones.
        for (int i = 0; i < countRecords; ++i) {

            assertTrue(priceUpdateDTO.getUpdates().get(i).getVersion().equals(
                    latestPriceVersions.get(i).getLatestPriceVersion()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getListing().
                    equals(latestPriceVersions.get(i).getListingId()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getFk_discount().equals(
                    latestPriceVersions.get(i).getPricingData().getFk_discount()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getMrp().equals(
                    latestPriceVersions.get(i).getPricingData().getMrp()));

            assertTrue(priceUpdateDTO.getUpdates().get(i).getFsp().equals(
                    latestPriceVersions.get(i).getPricingData().getFsp()));
        }

        assertTrue(priceUpdateDTO.getNextVersion() > priceUpdateDTO.getUpdates().get(countRecords - 1).getVersion());
    }
}

