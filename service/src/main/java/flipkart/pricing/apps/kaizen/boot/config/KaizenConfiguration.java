package flipkart.pricing.apps.kaizen.boot.config;

import io.dropwizard.Configuration;

/**
 * @understands DW Config class for Kaizen App
 */
public class KaizenConfiguration extends Configuration {
    // This is the only point of coupling between debian scripts that start the App passing this config to DW & the spring components that read this config
    public static final String YML_CONFIG_FILE = "pricing_kaizen.yaml";
}
