package com.transporeon.routing.service;

import com.transporeon.routing.Route;

import java.util.Optional;

public interface RoutingService {
    Optional<Route> findRoute(String iataCode1, String iataCode2);
}
