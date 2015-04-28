package flipkart.pricing.apps.kaizen.boot.healthcheck;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.stereotype.Component;

/**
 * @understands Control of App Rotation Status
 */
@Component("rotationStatus")
public class RotationStatusHealthCheck extends HealthCheck {
    private final Boolean inRotation;
    // TODO: In Rotation should be a bean which is played around with Admin Port


    public RotationStatusHealthCheck() {
        inRotation = true;
    }

    @Override
    protected Result check() throws Exception {
        if (inRotation) {
            return Result.healthy();
        }
        return Result.unhealthy("Not in rotation");
    }
}
