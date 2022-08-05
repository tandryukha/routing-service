package com.transporeon.routing.entity;

import lombok.Data;

@Data
public class Flight {
    /**
     * 3-letter (IATA) or 4-letter (ICAO) code of the source airport.
     */
    private String sourceAirport;
    /**
     * 3-letter (IATA) or 4-letter (ICAO) code of the destination airport.
     */
    private String destinationAirport;
}
