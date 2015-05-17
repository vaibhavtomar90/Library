package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.api.SignalResponseDto;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SignalResponseDtoMatcher extends BaseMatcher<SignalResponseDto> {
    private SignalResponseDto expectedDto;

    public static Matcher<SignalResponseDto> isEquivalent(SignalResponseDto expectedDto) {
        return new SignalResponseDtoMatcher(expectedDto);
    }

    private SignalResponseDtoMatcher(SignalResponseDto expectedDto) {
        this.expectedDto = expectedDto;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof SignalResponseDto)) return false;
        SignalResponseDto givenItem = (SignalResponseDto) item;
        if (givenItem.equals(expectedDto)) return true;
        if (!givenItem.getListing().equals(expectedDto.getListing())) return false;
        if (!givenItem.getListingVersion().equals(expectedDto.getListingVersion())) return false;
        if (!expectedDto.getSignals().containsAll(givenItem.getSignals())) return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(this.expectedDto);
    }
}
