package com.nearsoft.training.travel.api.dao;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Flight {
    private String origin;
    private String destination;
    private Date departureDate;
}
