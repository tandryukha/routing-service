package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Airport;

import java.util.List;

public interface AirportRepository {
    List<Airport> findAll();
}
