package flipkart.pricing.apps.kaizen.db.service;


import flipkart.pricing.apps.kaizen.api.SignalRequestDetail;
import flipkart.pricing.apps.kaizen.api.SignalRequestDto;
import flipkart.pricing.apps.kaizen.api.SignalResponseDetail;
import flipkart.pricing.apps.kaizen.api.SignalResponseDto;
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

import java.util.ArrayList;
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
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 1l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        signalService.updateSignals(signalRequestDto);
    }

    @Test(expected = SignalValueInvalidException.class)
    public void shouldThrowExceptionIfSignalValueIsInvalid() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.DOUBLE, null));
        //Add signals for foo listing
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "bar", 0l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        signalService.updateSignals(signalRequestDto);
    }

    @Test(expected = InvalidQualifierException.class)
    public void shouldThrowExceptionIfNoQualifierIsProvidedForPriceTypeSignal() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.PRICE, null));
        //Add signals for foo listing
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 0l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        signalService.updateSignals(signalRequestDto);
    }

    @Test(expected = InvalidQualifierException.class)
    public void shouldThrowExceptionIfInvalidQualifierIsProvidedForPriceTypeSignal() {
        SignalDao signalDao = new SignalDao(hibernateSessionTestRule.getSessionFactory());
        ListingInfoDao listingInfoDao = new ListingInfoDao(hibernateSessionTestRule.getSessionFactory());
        SignalTypeDao signalTypeDao = new SignalTypeDao(hibernateSessionTestRule.getSessionFactory());
        SignalService signalService = new SignalService(signalDao, listingInfoDao, signalTypeDao);
        signalTypeDao.insertSignalType(new SignalTypes("mrp", SignalDataTypes.PRICE, null));
        //Add signals for foo listing
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 0l, "USD"));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        signalService.updateSignals(signalRequestDto);
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
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 0l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        boolean versionUpdated = signalService.updateSignals(signalRequestDto);
        assertTrue(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        Signal signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("45.0"));
        assertTrue(signal.getVersion() == 0l);
        //Try to update signals with a newer version
        List<SignalRequestDetail> signalRequestDetails1 = Arrays.asList(new SignalRequestDetail("mrp", "55.0", 1l));
        SignalRequestDto signalRequestDto1 = new SignalRequestDto("foo", signalRequestDetails1);
        versionUpdated = signalService.updateSignals(signalRequestDto1);
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
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 1l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        boolean versionUpdated = signalService.updateSignals(signalRequestDto);
        assertTrue(versionUpdated);
        ListingInfo fetchedListingInfo = listingInfoDao.fetchListingByNameWithReadLock("foo");
        assertTrue(fetchedListingInfo.getVersion() == 1l);
        Signal signal = signalDao.fetchSignals(fetchedListingInfo.getId(), signalTypes.get("mrp").getId());
        assertTrue(signal.getValue().equals("45.0"));
        assertTrue(signal.getVersion() == 1l);
        //Try to update signals with an older version
        List<SignalRequestDetail> signalRequestDetails1 = Arrays.asList(new SignalRequestDetail("mrp", "55.0", 0l));
        SignalRequestDto signalRequestDto1 = new SignalRequestDto("foo", signalRequestDetails1);
        versionUpdated = signalService.updateSignals(signalRequestDto1);
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
        List<SignalRequestDetail> signalRequestDetails = Arrays.asList(new SignalRequestDetail("mrp", "45.0", 0l));
        SignalRequestDto signalRequestDto = new SignalRequestDto("foo", signalRequestDetails);
        signalService.updateSignals(signalRequestDto);
        SignalResponseDto signalResponseDto = signalService.fetchSignals("foo");
        assertTrue(signalResponseDto.getListing().equals("foo"));
        assertTrue(signalResponseDto.getListingVersion() == 1l);
        List<SignalResponseDetail> expectedSignalDetails = Arrays.asList(new SignalResponseDetail("mrp", "45.0", SignalDataTypes.DOUBLE),
                                                                         new SignalResponseDetail("ssp", "1.0", SignalDataTypes.DOUBLE));
        assertTrue(expectedSignalDetails.equals(signalResponseDto.getSignals()));
    }

}
