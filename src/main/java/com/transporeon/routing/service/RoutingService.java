package com.transporeon.routing.service;

import com.transporeon.routing.Route;

public interface RoutingService {
    Route findRoute(String iataCode1, String iataCode2);
}
