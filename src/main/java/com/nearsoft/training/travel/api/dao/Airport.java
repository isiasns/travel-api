package com.nearsoft.training.travel.api.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    private String iataCode;
    private String name;

    @JsonSetter(value = "value")
    public void setValue(String iataCode) {
        this.iataCode = iataCode;
    }

    @JsonSetter(value = "label")
    public void setLabel(String name) {
        this.name = name;
    }
}
