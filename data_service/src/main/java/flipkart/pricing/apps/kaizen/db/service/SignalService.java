package flipkart.pricing.apps.kaizen.db.service;


import flipkart.pricing.apps.kaizen.api.SignalFetchDetail;
import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
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
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
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

    public boolean updateSignals(SignalSaveDto signalSaveDto) {
        ListingInfo listingInfo = listingInfoDao.fetchListingByNameWithWriteLock(signalSaveDto.getListing()); //find with lock
        if (listingInfo == null) {
            logger.debug("Creating new listing "+ signalSaveDto.getListing());
            listingInfo = listingInfoDao.insertIgnoreListing(signalSaveDto.getListing()); //this will again take a write lock
        }
        //TODO Do we need sorting on signals level ?? I can't think of the use if we have listing level sorting already but maybe I'm missing something
        List<Signal> signals = convertToSignals(listingInfo.getId(), signalSaveDto.getSignals());
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

    public SignalFetchDto fetchSignals(String listing) {
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
        List<SignalFetchDetail> signalFetchDetailsList = new ArrayList<>();
        for (SignalTypes signalType : signalTypes) {
            Long signalId = signalType.getId();
            signalFetchDetailsList.add(new SignalFetchDetail(signalType.getName(),
                                          signalValueMap.get(signalId),
                                          signalType.getType()));
        }
        return new SignalFetchDto(listing, listingInfo.getVersion(), signalFetchDetailsList);
    }


    private List<Signal> convertToSignals(Long listingId, List<SignalSaveDetail> signalSaveDetailList) {
        List<Signal> signals = new ArrayList<>();
        Map<String, SignalTypes> signalTypesMap = signalTypeDao.fetchNameSignalTypesMap();
        for(SignalSaveDetail signalSaveDetail : signalSaveDetailList) {
            SignalTypes signalType = signalTypesMap.get(signalSaveDetail.getName());
            //All the error handling
            if (signalType == null) {
                logger.error("No entry present for signal name: "+ signalSaveDetail.getName());
                throw new SignalNameNotFoundException(signalSaveDetail.getName());
            }
            if (!signalType.getType().isValid(signalSaveDetail.getValue())) {
                logger.error(signalSaveDetail.getValue()+" is invalid value for signal name: "+ signalSaveDetail.getName());
                throw new SignalValueInvalidException(signalSaveDetail.getValue(), signalSaveDetail.getName());
            }
            if (signalType.getType().needsQualifier() && !ACCEPTED_QUALIFIER.equals(signalSaveDetail.getQualifier())) {
                logger.error("Incorrect qualifier for signal "+ signalSaveDetail.getName()+" : "+ signalSaveDetail.getQualifier());
                throw new InvalidQualifierException();
            }
            signals.add(new Signal(listingId, signalType.getId(), signalSaveDetail.getValue(), signalSaveDetail.getVersion(),
                                   signalSaveDetail.getQualifier()));
        }
        return signals;
    }

}
