package com.nearsoft.training.travel.api.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String number;
    private String origin;
    private String originTerminal;
    private String destination;
    private String destinationTerminal;
    private Date departureDate;
    private Date arrivalDate;
    private String airline;
}
