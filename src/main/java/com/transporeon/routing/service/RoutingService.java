package com.transporeon.routing.service;

import com.transporeon.routing.model.Route;
import com.transporeon.routing.entity.Airport;

import java.util.Optional;

public interface RoutingService {
    Optional<Route<Airport>> findRoute(String iataCode1, String iataCode2);
}
