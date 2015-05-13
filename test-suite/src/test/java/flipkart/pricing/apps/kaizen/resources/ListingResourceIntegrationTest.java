package flipkart.pricing.apps.kaizen.resources;

import flipkart.pricing.apps.kaizen.api.SellerListingData;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.testrules.SpringAwareResourceTestRule;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {KaizenContextConfiguration.class})
@ActiveProfiles("test")
public class ListingResourceIntegrationTest {

    @Rule
    @Autowired
    public SpringAwareResourceTestRule resources;

    @Test
    public void shouldRespondWith_OK_toSaveData() throws Exception {
        final Response response = resources.client().target("/listing/foo").request().
            header(ListingResource.CLIENT_ID_HEADER_KEY, "fooBar").
            post(Entity.json(new SellerListingData(10.2, 20.1, 100l)));
        assertThat(IOUtils.toString((InputStream) response.getEntity()), is("OK"));
    }
}