package flipkart.pricing.apps.kaizen.converters;

import flipkart.pricing.apps.kaizen.api.ListingUpdateMessage;
import flipkart.pricing.apps.kaizen.api.SignalSaveDto;
import org.apache.camel.Converter;

/**
 * @understands Camel Compatible converter that converts a given SignalSaveDto to ListingUpdateMessage
 */
@Converter
public class SignalSaveDtoToListingUpdateMessageConverter {
    @Converter
    public static ListingUpdateMessage convert(SignalSaveDto signalSaveDto) {
        return new ListingUpdateMessage(signalSaveDto.getListing());
    }
}
