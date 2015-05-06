package flipkart.pricing.apps.kaizen.db.service;


import flipkart.pricing.apps.kaizen.db.dao.ListingInfoDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalDao;
import flipkart.pricing.apps.kaizen.db.dao.SignalTypeDao;
import flipkart.pricing.apps.kaizen.db.model.ListingInfo;
import flipkart.pricing.apps.kaizen.db.model.Signal;
import flipkart.pricing.apps.kaizen.db.model.SignalId;
import flipkart.pricing.apps.kaizen.db.model.SignalTypes;
import flipkart.pricing.apps.kaizen.dto.SignalFetchDto;
import flipkart.pricing.apps.kaizen.dto.SignalUpdateDto;

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

    public boolean updateSignals(String listing, List<SignalUpdateDto> signalUpdateDtoList) {
        ListingInfo listingInfo = listingInfoDao.insertIgnoreListing(listing);
        List<Signal> signals = convertToSignals(listingInfo.getId(), signalUpdateDtoList);
        int signalsUpdatedCount = 0;
        for (Signal signal: signals) {
            signalsUpdatedCount += signalDao.insertOrUpdateSignal(signal);
        }
        if (signalsUpdatedCount > 0) {
            listingInfoDao.updateVersionAndGetListing(listingInfo);
            return true;
        }
        return false;
    }

    public List<SignalFetchDto> fetchSignals(String listing) {
        ListingInfo listingInfo = listingInfoDao.fetchListingByName(listing);
        List<SignalTypes> signalTypes = signalTypeDao.fetchAll();
        Map<Long, String> signalValueMap = new HashMap<>();
        for (SignalTypes signalType : signalTypes) {
            signalValueMap.put(signalType.getId(), signalType.getDefaultValue());
        }
        List<Signal> signals = signalDao.fetchAllForListing(listingInfo.getId());
        for (Signal signal : signals) {
            signalValueMap.put(signal.getId().getSignalTypeId(), signal.getValue());
        }
        List<SignalFetchDto> signalFetchDtoList = new ArrayList<>();
        for (SignalTypes signalType : signalTypes) {
            Long signalId = signalType.getId();
            signalFetchDtoList.add(new SignalFetchDto(signalType.getName(), signalValueMap.get(signalId),
                                   signalType.getType()));
        }
        return signalFetchDtoList;
    }


    private List<Signal> convertToSignals(Long listingId, List<SignalUpdateDto> signalUpdateDtoList) {
        List<Signal> signals = new ArrayList<>();
        Map<String, SignalTypes> signalTypesMap = signalTypeDao.fetchNameSignalTypesMap();
        for(SignalUpdateDto signalUpdateDto : signalUpdateDtoList) {
            SignalTypes signalType = signalTypesMap.get(signalUpdateDto.getSignalName());
            signals.add(new Signal(new SignalId(listingId, signalType.getId()), signalUpdateDto.getSignalValue(),
                                   signalUpdateDto.getVersion(), signalUpdateDto.getSignalExpiry()));
        }
        return signals;
    }
}
