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
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import flipkart.pricing.apps.kaizen.exceptions.InvalidQualifierException;
import flipkart.pricing.apps.kaizen.exceptions.ListingNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalNameNotFoundException;
import flipkart.pricing.apps.kaizen.exceptions.SignalValueInvalidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalService {

    private static final String ACCEPTED_QUALIFIER = "INR";
    private static final Logger logger = LoggerFactory.getLogger(SignalService.class);
    private SignalDao signalDao;
    private ListingInfoDao listingInfoDao;
    private SignalTypeDao signalTypeDao;


    @Inject
    public SignalService(SignalDao signalDao, ListingInfoDao listingInfoDao, SignalTypeDao signalTypeDao) {
        this.signalDao = signalDao;
        this.listingInfoDao = listingInfoDao;
        this.signalTypeDao = signalTypeDao;
    }

    public boolean updateSignals(SignalRequestDto signalRequestDto) {
        ListingInfo listingInfo = listingInfoDao.fetchListingByNameWithWriteLock(signalRequestDto.getListing()); //find with lock
        if (listingInfo == null) {
            logger.debug("Creating new listing "+signalRequestDto.getListing());
            listingInfo = listingInfoDao.insertIgnoreListing(signalRequestDto.getListing()); //this will again take a write lock
        }
        //TODO Do we need sorting on signals level ?? I can't think of the use if we have listing level sorting already but maybe I'm missing something
        List<Signal> signals = convertToSignals(listingInfo.getId(), signalRequestDto.getSignalRequestDetails());
        int signalsUpdatedCount = 0;
        for (Signal signal: signals) {
            signalsUpdatedCount += signalDao.insertOrUpdateSignal(signal);
        }
        logger.debug("Total updated signal count for "+listingInfo.getListing()+" : "+signalsUpdatedCount);
        if (signalsUpdatedCount > 0) {
            logger.debug("Bumping up version for listing: "+listingInfo.getListing());
            listingInfoDao.updateVersionAndGetListing(listingInfo.getListing());
            return true;
        }
        return false;
    }

    public SignalResponseDto fetchSignals(String listing) {
        ListingInfo listingInfo = listingInfoDao.fetchListingByNameWithReadLock(listing);
        if (listingInfo == null) {
            throw new ListingNotFoundException(listing);
        }
        List<SignalTypes> signalTypes = signalTypeDao.fetchAll();
        Map<Long, String> signalValueMap = new HashMap<>();
        //Populating the default values
        for (SignalTypes signalType : signalTypes) {
            signalValueMap.put(signalType.getId(), signalType.getDefaultValue());
        }
        List<Signal> signals = signalDao.fetchSignals(listingInfo.getId());
        //Overriding the default values, if values are explicitly present
        for (Signal signal : signals) {
            signalValueMap.put(signal.getId().getSignalTypeId(), signal.getValue());
        }
        List<SignalResponseDetail> signalResponseDetailsList = new ArrayList<>();
        for (SignalTypes signalType : signalTypes) {
            Long signalId = signalType.getId();
            signalResponseDetailsList.add(new SignalResponseDetail(signalType.getName(),
                                          signalValueMap.get(signalId),
                                          signalType.getType()));
        }
        return new SignalResponseDto(listing, listingInfo.getVersion(), signalResponseDetailsList);
    }


    private List<Signal> convertToSignals(Long listingId, List<SignalRequestDetail> signalRequestDetailList) {
        List<Signal> signals = new ArrayList<>();
        Map<String, SignalTypes> signalTypesMap = signalTypeDao.fetchNameSignalTypesMap();
        for(SignalRequestDetail signalRequestDetail : signalRequestDetailList) {
            SignalTypes signalType = signalTypesMap.get(signalRequestDetail.getName());
            //All the error handling
            if (signalType == null) {
                logger.error("No entry present for signal name: "+signalRequestDetail.getName());
                throw new SignalNameNotFoundException(signalRequestDetail.getName());
            }
            if (!signalType.getType().isValid(signalRequestDetail.getValue())) {
                logger.error(signalRequestDetail.getValue()+" is invalid value for signal name: "+signalRequestDetail.getName());
                throw new SignalValueInvalidException(signalRequestDetail.getValue(), signalRequestDetail.getName());
            }
            if (signalType.getType().needsQualifier() && !ACCEPTED_QUALIFIER.equals(signalRequestDetail.getQualifier())) {
                logger.error("Incorrect qualifier for signal "+signalRequestDetail.getName()+" : "+signalRequestDetail.getQualifier());
                throw new InvalidQualifierException();
            }
            //TODO define how to set server_timestamp
            signals.add(new Signal(listingId, signalType.getId(), signalRequestDetail.getValue(), signalRequestDetail.getVersion(),
                                   new Timestamp(System.currentTimeMillis()), signalRequestDetail.getQualifier()));
        }
        return signals;
    }

}
