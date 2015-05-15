package flipkart.pricing.apps.kaizen.resources;

import flipkart.pricing.apps.kaizen.api.SignalRequestDetail;
import flipkart.pricing.apps.kaizen.api.SignalRequestDto;
import flipkart.pricing.apps.kaizen.api.SignalResponseDetail;
import flipkart.pricing.apps.kaizen.api.SignalResponseDto;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import flipkart.pricing.apps.kaizen.db.service.SignalService;
import flipkart.pricing.apps.kaizen.testrules.SpringAwareResourceTestRule;
import org.apache.camel.CamelContext;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.InetAddress;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
public class ListingResourceIntegrationTest {

    @Rule
    @Inject
    public SpringAwareResourceTestRule resources;

    @Inject
    public SignalService signalService;

    @Inject
    public SignalTypeDao signalTypeDao;

    @Inject
    public CamelContext camelContext;
    
    @Test
    @Rollback
    @Transactional
    public void shouldSaveListingDataToDB_ForNewListing() throws Exception {
        //TODO : Move this line to setup
        signalTypeDao.insertSignalType(new SignalTypes("s1", SignalDataTypes.STRING, "blah"));
        final Response response = resources.client().target("/v1/listings").request().
            header(ListingResource.CLIENT_ID_HEADER_KEY, "fooBar").
            post(Entity.json(new SignalRequestDto("lst1", singletonList(new SignalRequestDetail("s1", "v1", 1l)), 1l)));
        assertThat(response.getStatus(), is(200));
        final SignalResponseDto fetchedSignalsForLst1 = signalService.fetchSignals("lst1");
        assertThat(fetchedSignalsForLst1,is(new SignalResponseDto("lst1",1l, singletonList(new SignalResponseDetail("s1", "v1", SignalDataTypes.STRING)))));
    }

    @Test
    public void testFoo() throws Exception {
        System.out.println("InetAddress.getLocalHost() = " + InetAddress.getLocalHost());

    }
}