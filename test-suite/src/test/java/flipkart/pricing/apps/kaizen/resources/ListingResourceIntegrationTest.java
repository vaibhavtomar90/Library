package flipkart.pricing.apps.kaizen.resources;

import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.api.SignalResponseDto;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.service.SignalService;
import flipkart.pricing.apps.kaizen.testrules.DbSetupRule;
import flipkart.pricing.apps.kaizen.testrules.SpringAwareResourceTestRule;
import flipkart.pricing.apps.kaizen.utils.HibernateSessionUtil;
import flipkart.pricing.apps.kaizen.utils.SignalDtoTestUtils;
import org.apache.camel.CamelContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static flipkart.pricing.apps.kaizen.utils.SignalResponseDtoMatcher.isEquivalent;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
public class ListingResourceIntegrationTest {

    @Rule
    @Inject
    public SpringAwareResourceTestRule resources;

    @Rule
    @Inject
    public DbSetupRule dbSetupRule;

    @Inject
    public SignalService signalService;

    @Inject
    public CamelContext camelContext;

    @Inject
    HibernateSessionUtil<SignalFetchDto> hibernateSessionUtil;
    
    @Test
    public void shouldSaveListingDataToDB_ForNewListing() throws Exception {
        final Response response = resources.client().target("/v1/listings").request().
            header(ListingResource.CLIENT_ID_HEADER_KEY, "fooBar").
            post(Entity.json(SignalDtoTestUtils.getSampleSignalRequestDto()));
        assertThat(response.getStatus(), is(200));
        final SignalFetchDto fetchedSignalsForLst1 = hibernateSessionUtil.withinSession(() -> signalService.fetchSignals("lst1"));
        assertThat(fetchedSignalsForLst1, isEquivalent(SignalDtoTestUtils.getSampleSignalResponseDto()));
        // TODO : assert that Message has been propagated to Kafka once the entire pipeline from Save -> Compute -> Propagate is built
    }

}