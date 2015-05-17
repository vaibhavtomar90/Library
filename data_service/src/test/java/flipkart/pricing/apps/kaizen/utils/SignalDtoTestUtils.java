package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.api.SignalRequestDetail;
import flipkart.pricing.apps.kaizen.api.SignalRequestDto;
import flipkart.pricing.apps.kaizen.api.SignalResponseDetail;
import flipkart.pricing.apps.kaizen.api.SignalResponseDto;
import flipkart.pricing.apps.kaizen.db.model.SignalDataTypes;
import flipkart.pricing.apps.kaizen.testrules.SignalTypesInjectionRule;

import java.util.Arrays;
import java.util.List;

public class SignalDtoTestUtils {

    public static final String DEFAULT_LISTING = "lst1";
    public static final long DEFAULT_LISTING_VERSION = 1l;

    public static SignalRequestDto getSampleSignalRequestDto() {
        return getSampleSignalRequestDto(DEFAULT_LISTING);
    }

    public static SignalRequestDto getSampleSignalRequestDto(String listingId) {
        return getSampleSignalRequestDto(listingId, DEFAULT_LISTING_VERSION);
    }

    public static SignalRequestDto getSampleSignalRequestDto(String listingId, long listingVersion) {
        return getSampleSignalRequestDto(listingId, listingVersion, Arrays.asList(
            new SignalRequestDetail(SignalTypesInjectionRule.BRAND_STRING_TYPE_SIGNALTYPE, "levi", 1l),
            new SignalRequestDetail(SignalTypesInjectionRule.MRP_PRICE_TYPE_SIGNALTYPE, "100.0", 1l, "INR"),
            new SignalRequestDetail(SignalTypesInjectionRule.ATP_INT_TYPE_SIGNALTYPE, "20", 1l)
        ));
    }

    public static SignalRequestDto getSampleSignalRequestDto(String listingId, long listingVersion, List<SignalRequestDetail> signalRequestDetails) {
        return new SignalRequestDto(listingId, signalRequestDetails, listingVersion);
    }

    public static SignalResponseDto getSampleSignalResponseDto() {
        return getSampleSignalResponseDto(DEFAULT_LISTING);
    }

    private static SignalResponseDto getSampleSignalResponseDto(String listingId) {
        return getSampleSignalResponseDto(listingId,DEFAULT_LISTING_VERSION);
    }

    public static SignalResponseDto getSampleSignalResponseDto(String listingId, long listingVersion) {
        return new SignalResponseDto(listingId,listingVersion,Arrays.asList(
            new SignalResponseDetail(SignalTypesInjectionRule.BRAND_STRING_TYPE_SIGNALTYPE,"levi", SignalDataTypes.STRING),
            new SignalResponseDetail(SignalTypesInjectionRule.MRP_PRICE_TYPE_SIGNALTYPE,"100.0", SignalDataTypes.PRICE),
            new SignalResponseDetail(SignalTypesInjectionRule.ATP_INT_TYPE_SIGNALTYPE,"20", SignalDataTypes.INT),
            new SignalResponseDetail(SignalTypesInjectionRule.PAGEHITS_LONG_TYPE_SIGNALTYPE,"0", SignalDataTypes.LONG),
            new SignalResponseDetail(SignalTypesInjectionRule.BAND_DOUBLE_TYPE_SIGNALTYPE,"0.0", SignalDataTypes.DOUBLE)
        ));
    }
}
