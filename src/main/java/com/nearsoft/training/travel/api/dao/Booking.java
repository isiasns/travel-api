package com.nearsoft.training.travel.api.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings",
        uniqueConstraints = {@UniqueConstraint(
                name = "booking_uk",
                columnNames = {"user_id", "departing_id", "returning_id"}
        )}
)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Flight departing;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Flight returning;
    private String status;

    public enum Status {
        TEMP,
        PAID
    }
}
