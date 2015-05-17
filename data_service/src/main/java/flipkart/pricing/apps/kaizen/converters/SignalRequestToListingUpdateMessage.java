package flipkart.pricing.apps.kaizen.converters;

import flipkart.pricing.apps.kaizen.api.ListingUpdateMessage;
import flipkart.pricing.apps.kaizen.api.SignalRequestDto;
import org.apache.camel.Converter;

/**
 * @understands Camel Compatible converter that converts a given SignalRequestDto to ListingUpdateMessage
 */
@Converter
public class SignalRequestToListingUpdateMessage {
    @Converter
    public static ListingUpdateMessage convert(SignalRequestDto signalRequestDto) {
        return new ListingUpdateMessage(signalRequestDto.getListing());
    }
}
