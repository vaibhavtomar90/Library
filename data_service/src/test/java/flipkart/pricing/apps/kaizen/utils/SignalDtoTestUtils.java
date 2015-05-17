package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.api.SignalFetchDetail;
import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import flipkart.pricing.apps.kaizen.api.SignalSaveDetail;
import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
import flipkart.pricing.apps.kaizen.db.model.SignalDataType;
import flipkart.pricing.apps.kaizen.testrules.SignalTypesInjectionRule;

import java.util.Arrays;
import java.util.List;

public class SignalDtoTestUtils {

    public static final String DEFAULT_LISTING = "lst1";
    public static final long DEFAULT_LISTING_VERSION = 1l;

    public static SignalSaveDto getSampleSignalSaveDto() {
        return getSampleSignalSaveDto(DEFAULT_LISTING);
    }

    public static SignalSaveDto getSampleSignalSaveDto(String listingId) {
        return getSampleSignalSaveDto(listingId, DEFAULT_LISTING_VERSION);
    }

    public static SignalSaveDto getSampleSignalSaveDto(String listingId, long listingVersion) {
        return getSampleSignalSaveDto(listingId, listingVersion, Arrays.asList(
            new SignalSaveDetail(SignalTypesInjectionRule.BRAND_STRING_TYPE_SIGNALTYPE, "levi", 1l),
            new SignalSaveDetail(SignalTypesInjectionRule.MRP_PRICE_TYPE_SIGNALTYPE, "100.0", 1l, "INR"),
            new SignalSaveDetail(SignalTypesInjectionRule.ATP_INT_TYPE_SIGNALTYPE, "20", 1l)
        ));
    }

    public static SignalSaveDto getSampleSignalSaveDto(String listingId, long listingVersion, List<SignalSaveDetail> signalRequestDetails) {
        return new SignalSaveDto(listingId, signalRequestDetails, listingVersion);
    }

    public static SignalFetchDto getSampleSignalFetchDto() {
        return getSampleSignalFetchDto(DEFAULT_LISTING);
    }

    private static SignalFetchDto getSampleSignalFetchDto(String listingId) {
        return getSampleSignalFetchDto(listingId, DEFAULT_LISTING_VERSION);
    }

    public static SignalFetchDto getSampleSignalFetchDto(String listingId, long listingVersion) {
        return new SignalFetchDto(listingId,listingVersion,Arrays.asList(
            new SignalFetchDetail(SignalTypesInjectionRule.BRAND_STRING_TYPE_SIGNALTYPE,"levi", SignalDataType.STRING),
            new SignalFetchDetail(SignalTypesInjectionRule.MRP_PRICE_TYPE_SIGNALTYPE,"100.0", SignalDataType.PRICE),
            new SignalFetchDetail(SignalTypesInjectionRule.ATP_INT_TYPE_SIGNALTYPE,"20", SignalDataType.INT),
            new SignalFetchDetail(SignalTypesInjectionRule.PAGEHITS_LONG_TYPE_SIGNALTYPE,"0", SignalDataType.LONG),
            new SignalFetchDetail(SignalTypesInjectionRule.BAND_DOUBLE_TYPE_SIGNALTYPE,"0.0", SignalDataType.DOUBLE)
        ));
    }
}
