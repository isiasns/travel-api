package com.nearsoft.training.travel.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("travel-api")
public class TravelApiConfig {
    private final String PARAMS = "?";
    private String apikey;
    private String url;
    private String version;
    private AirportsApi airportsApi;
    private FlightsApi flightsApi;

    private String getTravelApiUrl() {
        return url + version;
    }

    private String getApikey() {
        return "apikey=" + apikey;
    }

    public String getAirportsAutocomplete() {
        return getTravelApiUrl() + airportsApi.getAutocompleteUrl() + PARAMS + getApikey();
    }

    public String getOneWaySearch() {
        return getTravelApiUrl() + flightsApi.getOneWaySearchUrl() + PARAMS + getApikey();
    }

    @Data
    public static class AirportsApi {
        private String url;
        private String autocomplete;

        public String getAutocompleteUrl() {
            return url + autocomplete;
        }
    }

    @Data
    public static class FlightsApi {
        private String url;
        private String oneWay;

        public String getOneWaySearchUrl() {
            return url + oneWay;
        }
    }
}
