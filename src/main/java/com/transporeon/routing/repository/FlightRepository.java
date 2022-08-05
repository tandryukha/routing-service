package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.entity.Flight;

import java.util.List;

public interface FlightRepository {
    List<Flight> findAll();
}
