package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoutingServiceImpl implements RoutingService {
    private final FlightRepository flightRepository;

    @Override
    public Route findRoute(String iataCode1, String iataCode2) {
        return new Route(iataCode1).add(iataCode2);
    }
}
