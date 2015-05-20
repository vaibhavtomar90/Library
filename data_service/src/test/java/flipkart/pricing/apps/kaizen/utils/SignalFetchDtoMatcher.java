package flipkart.pricing.apps.kaizen.utils;

import flipkart.pricing.apps.kaizen.api.SignalFetchDto;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class SignalFetchDtoMatcher extends BaseMatcher<SignalFetchDto> {
    private SignalFetchDto expectedDto;

    public static Matcher<SignalFetchDto> isEquivalent(SignalFetchDto expectedDto) {
        return new SignalFetchDtoMatcher(expectedDto);
    }

    private SignalFetchDtoMatcher(SignalFetchDto expectedDto) {
        this.expectedDto = expectedDto;
    }

    @Override
    public boolean matches(Object item) {
        if (!(item instanceof SignalFetchDto)) return false;
        SignalFetchDto givenItem = (SignalFetchDto) item;
        if (givenItem.equals(expectedDto)) return true;
        if (!givenItem.getListing().equals(expectedDto.getListing())) return false;
        if (!givenItem.getVersion().equals(expectedDto.getVersion())) return false;
        if (!expectedDto.getSignals().containsAll(givenItem.getSignals())) return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(this.expectedDto);
    }
}
