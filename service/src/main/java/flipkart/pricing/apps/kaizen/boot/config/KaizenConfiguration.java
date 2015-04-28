package flipkart.pricing.apps.kaizen.boot.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @understands DW Config class for Kaizen App
 */
public class KaizenConfiguration extends Configuration {
    // This is the only point of coupling between debian scripts that start the App passing this config to DW & the spring components that read this config
    public static final String YML_CONFIG_FILE = "kaizen-configuration.yml";

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    public DataSourceFactory getDataSourceFactory() {
        database.build(null,null);
        return database;
    }
}
