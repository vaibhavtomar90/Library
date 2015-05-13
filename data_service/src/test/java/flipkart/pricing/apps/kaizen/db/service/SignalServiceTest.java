package flipkart.pricing.apps.kaizen.db.service;


import flipkart.pricing.apps.kaizen.api.SignalFetchDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.db.dao.ListingInfoDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.db.model.Signal;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import flipkart.pricing.apps.kaizen.exceptions.InvalidQualifierException;
import flipkart.pricing.apps.kaizen.exceptions.ListingNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalNameNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalValueInvalidException;
import flipkart.pricing.apps.kaizen.testrules.HibernateSessionTestRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class SignalServiceTest {

    @Rule
    public HibernateSessionTestRule hibernateSessionTestRule = new HibernateSessionTestRule();


    @Test(expected = SignalNameNotFoundException.class)
    public void shouldThrowExceptionIfSignalTypeDoesNotExist() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 1l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = SignalValueInvalidException.class)
    public void shouldThrowExceptionIfSignalValueIsInvalid() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.DOUBLE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "bar", 0l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = InvalidQualifierException.class)
    public void shouldThrowExceptionIfNoQualifierIsProvidedForPriceTypeSignal() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.PRICE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test(expected = InvalidQualifierException.class)
    public void shouldThrowExceptionIfInvalidQualifierIsProvidedForPriceTypeSignal() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.PRICE, null));
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l, "USD"));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
    }

    @Test
    public void shouldUpdateListingVersionIfAnySignalChanges() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        //Add signal types
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.DOUBLE, null));
        Map<String, SignalTypes> signalTypes = signalTypeDao.fetchNameSignalTypesMap();
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        boolean versionUpdated = signalService.updateSignals(signalSaveDto);
        assertTrue(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        Signal signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("45.0"));
        assertTrue(signal.getVersion() == 0l);
        //Try to update signals with a newer version
        List<SignalSaveDetail> signalSaveDetails1 = Arrays.asList(new SignalSaveDetail("mrp", "55.0", 1l));
        SignalSaveDto signalSaveDto1 = new SignalSaveDto("foo", signalSaveDetails1);
        versionUpdated = signalService.updateSignals(signalSaveDto1);
        assertTrue(versionUpdated);
        fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 2l);
        signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("55.0"));
        assertTrue(signal.getVersion() == 1l);
    }

    @Test
    public void shouldNotUpdateListingVersionIfNoSignalChanges() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        //Add signal types
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.DOUBLE, null));
        Map<String, SignalTypes> signalTypes = signalTypeDao.fetchNameSignalTypesMap();
        //Add signals for foo listing
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 1l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        boolean versionUpdated = signalService.updateSignals(signalSaveDto);
        assertTrue(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        Signal signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("45.0"));
        assertTrue(signal.getVersion() == 1l);
        //Try to update signals with an older version
        List<SignalSaveDetail> signalSaveDetails1 = Arrays.asList(new SignalSaveDetail("mrp", "55.0", 0l));
        SignalSaveDto signalSaveDto1 = new SignalSaveDto("foo", signalSaveDetails1);
        versionUpdated = signalService.updateSignals(signalSaveDto1);
        assertFalse(versionUpdated);
        fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("45.0"));
        assertTrue(signal.getVersion() == 1l);
    }

    @Test(expected = ListingNotFoundException.class)
    public void shouldThrowExceptionIfListingIfNotPresentForFetch() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalService.fetchSignals("foo");
    }

    @Test
    public void shouldReturnDefaultValuesInFetchIfSignalIsNotPresent() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        //Add signal types
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.DOUBLE, "1.0"));
        signalTypeDao.insertSignalType(new SignalTypes("ssp", SignalDataTypes.DOUBLE, "1.0"));
        List<SignalSaveDetail> signalSaveDetails = Arrays.asList(new SignalSaveDetail("mrp", "45.0", 0l));
        SignalSaveDto signalSaveDto = new SignalSaveDto("foo", signalSaveDetails);
        signalService.updateSignals(signalSaveDto);
        SignalFetchDto signalFetchDto = signalService.fetchSignals("foo");
        assertTrue(signalFetchDto.getListing().equals("foo"));
        assertTrue(signalFetchDto.getVersion() == 1l);
        List<SignalFetchDetail> expectedSignalDetails = Arrays.asList(new SignalFetchDetail("mrp", "45.0", SignalDataTypes.DOUBLE),
                                                                         new SignalFetchDetail("ssp", "1.0", SignalDataTypes.DOUBLE));
        assertTrue(expectedSignalDetails.equals(signalFetchDto.getSignals()));
    }

}
