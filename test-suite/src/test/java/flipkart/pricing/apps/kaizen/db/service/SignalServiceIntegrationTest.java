package flipkart.pricing.apps.kaizen.db.service;


import flipkart.pricing.apps.kaizen.api.SignalFetchDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.boot.config.KaizenContextConfiguration;
import flipkart.pricing.apps.kaizen.db.dao.ListingInfoDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalInfoDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.db.model.SignalInfo;
import flipkart.pricing.apps.kaizen.db.model.SignalType;
import flipkart.pricing.apps.kaizen.exceptions.InvalidQualifierException;
import flipkart.pricing.apps.kaizen.exceptions.ListingNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalNameNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalValueInvalidException;
import flipkart.pricing.apps.kaizen.testrules.DbClearRule;
import flipkart.pricing.apps.kaizen.utils.SignalDtoTestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KaizenContextConfiguration.class)
@ActiveProfiles("test")
public class SignalServiceIntegrationTest {

    @Inject
    private SignalService signalService;

    @Inject
    private SignalInfoDao signalInfoDao;

    @Inject
    private SignalTypeDao signalTypeDao;

    @Inject
    private ListingInfoDao listingInfoDao;

    @Rule
    @Inject
    public DbClearRule dbClearRule;

    @Test(expected = SignalNameNotFoundException.class)
    @Transactional
    public void shouldThrowExceptionIfSignalTypeDoesNotExist() {
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 1l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = SignalValueInvalidException.class)
    @Transactional
    public void shouldThrowExceptionIfSignalValueIsInvalid() {
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.DOUBLE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "bar", 0l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = InvalidQualifierException.class)
    @Transactional
    public void shouldThrowExceptionIfNoQualifierIsProvidedForPriceTypeSignal() {
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.PRICE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);;
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = InvalidQualifierException.class)
    @Transactional
    public void shouldThrowExceptionIfInvalidQualifierIsProvidedForPriceTypeSignal() {
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.PRICE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l, "USD"));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);;
        signalService.updateSignals(signalSaveDto);
    }

    @Test
    @Transactional
    public void shouldUpdateListingVersionIfAnySignalChanges() {
        //Add signalInfo types
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.DOUBLE, null));
        Map<String, SignalType> signalTypes = signalTypeDao.fetchNameSignalTypesMap();
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);;
        boolean versionUpdated = signalService.updateSignals(signalSaveDto);
        assertTrue(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        SignalInfo signalInfo = signalInfoDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signalInfo.getValue().equals("45.0"));
        assertTrue(signalInfo.getVersion() == 0l);
        //Try to update signals with a newer version
        List<SignalSaveDetail> signalSaveDetails1 = Arrays.asList(new SignalSaveDetail("mrp", "55.0", 1l));
        SignalSaveDto signalSaveDto1 = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails1);
        versionUpdated = signalService.updateSignals(signalSaveDto1);
        assertTrue(versionUpdated);
        fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 2l);
        signalInfo = signalInfoDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signalInfo.getValue().equals("55.0"));
        assertTrue(signalInfo.getVersion() == 1l);
    }

    @Test
    @Transactional
    public void shouldNotUpdateListingVersionIfNoSignalChanges() {
        //Add signalInfo types
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.DOUBLE, null));
        Map<String, SignalType> signalTypes = signalTypeDao.fetchNameSignalTypesMap();
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 1l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);;
        signalService.updateSignals(signalSaveDto);
        //Try to update signals with an older version
        List<SignalSaveDetail> signalSaveDetails1 = Arrays.asList(new SignalSaveDetail("mrp", "55.0", 0l));
        SignalSaveDto signalSaveDto1 = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails1);
        boolean versionUpdated = signalService.updateSignals(signalSaveDto1);
        assertFalse(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        SignalInfo signalInfo = signalInfoDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signalInfo.getValue().equals("45.0"));
        assertTrue(signalInfo.getVersion() == 1l);
    }

    @Test(expected = ListingNotFoundException.class)
    @Transactional
    public void shouldThrowExceptionIfListingIfNotPresentForFetch() {
        signalService.fetchSignals("foo");
    }

    @Test
    @Transactional
    public void shouldReturnDefaultValuesInFetchIfSignalIsNotPresent() {
        //Add signal types
        signalTypeDao.insertSignalType(new SignalType("mrp", SignalDataType.DOUBLE, "1.0"));
        signalTypeDao.insertSignalType(new SignalType("ssp", SignalDataType.DOUBLE, "1.0"));
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = SignalDtoTestUtils.getSampleSignalSaveDto("foo", 1l, signalSaveDetails);;
        signalService.updateSignals(signalSaveDto);
        SignalFetchDto signalFetchDto = signalService.fetchSignals("foo");
        assertTrue(signalFetchDto.getListing().equals("foo"));
        assertTrue(signalFetchDto.getVersion() == 1l);
        List<SignalFetchDetail> expectedSignalDetails = Arrays.asList(new SignalFetchDetail("mrp", "45.0", SignalDataType.DOUBLE),
            new SignalFetchDetail("ssp", "1.0", SignalDataType.DOUBLE));
        assertTrue(expectedSignalDetails.equals(signalFetchDto.getSignals()));
    }

}
