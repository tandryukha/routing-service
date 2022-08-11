package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Airport;

import java.util.List;
import java.util.Map;

public interface AirportRepository {
    List<Airport> findAll();

    boolean isPresent(String iataCode);

    Map<String, Airport> findAllGrouped();
}
