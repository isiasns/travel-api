package com.nearsoft.training.travel.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "travel-api")
public class TravelApiConfig {
    private static String apiKey;
    private static String url;
    private static String version;
    private static final String PARAMS = "?";

    private static String getTravelApiUrl() {
        return url + version;
    }

    private static String getApiKey() {
        return "apiKey=" + apiKey;
    }

    public static String getAirportsAutocomplete() {
        return getTravelApiUrl() + AirportsConfig.getAutocompleteUrl() + PARAMS + getApiKey();
    }

    @Data
    @ConfigurationProperties(prefix = "airports-api")
    public static class AirportsConfig {
        private static String url;
        private static String autocomplete;

        public static String getAutocompleteUrl() {
            return url + autocomplete;
        }
    }
}
