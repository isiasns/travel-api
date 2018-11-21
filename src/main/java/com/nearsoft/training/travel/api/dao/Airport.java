package com.nearsoft.training.travel.api.dao;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Airport {
    private String iataCode;
    private String name;
}
