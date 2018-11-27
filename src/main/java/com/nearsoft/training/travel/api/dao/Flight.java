package com.nearsoft.training.travel.api.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private String origin;
    private String originTerminal;
    private String destination;
    private String destinationTerminal;
    private Date departureDate;
    private Date arrivalDate;
    private String number;
    private String airline;
}
