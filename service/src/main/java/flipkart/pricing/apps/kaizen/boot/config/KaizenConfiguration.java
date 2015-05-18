package flipkart.pricing.apps.kaizen.boot.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * @understands DW Config class for Kaizen App
 */
public class KaizenConfiguration extends Configuration {
    // This is the only point of coupling between debian scripts that start the App passing this config to DW & the spring components that read this config
    public static final String YML_CONFIG_FILE = "pricing_kaizen.yaml";

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * DW also uses the same yaml file as spring.
     * THus, we needed to add this Map as a placeholder for all configs that are defined under the heading 'kaizen' & are actually used by spring*
     * This map is not & should not be used anywhere in the code.
     */
    @Valid
    @NotNull
    @JsonProperty
    private Map<String,Object> kaizen = new HashMap<>();

    public DataSourceFactory getDataSourceFactory() {
        database.build(null,null);
        return database;
    }
}