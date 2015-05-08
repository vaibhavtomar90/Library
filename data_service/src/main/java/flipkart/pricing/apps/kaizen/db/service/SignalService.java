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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalService {

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
            listingInfo = listingInfoDao.insertIgnoreListing(signalRequestDto.getListing()); //this will take lock throughout the transaction
        }
        //TODO Do we need sorting on signals level ?? I can't think of the use if we have listing level sorting already but maybe I'm missing something
        List<Signal> signals = convertToSignals(listingInfo.getId(), signalRequestDto.getSignalRequestDetails());
        int signalsUpdatedCount = 0;
        for (Signal signal: signals) {
            signalsUpdatedCount += signalDao.insertOrUpdateSignal(signal);
        }
        if (signalsUpdatedCount > 0) {
            listingInfoDao.updateVersionAndGetListing(listingInfo.getListing());
            return true;
        }
        return false;
    }

    public SignalResponseDto fetchSignals(String listing) {
        ListingInfo listingInfo = listingInfoDao.fetchListingByNameWithReadLock(listing);
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
            signals.add(new Signal(listingId, signalType.getId(), signalRequestDetail.getValue(), signalRequestDetail.getVersion(),
                                   null, signalRequestDetail.getQualifier())); //TODO define how to set server_timestamp
        }
        return signals;
    }

}
