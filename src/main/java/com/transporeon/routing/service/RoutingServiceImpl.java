package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoutingServiceImpl implements RoutingService {
    private final AirportRepository airportRepository;

    @Override
    public Route findRoute(String iataCode1, String iataCode2) {
        return new Route(iataCode1).add(iataCode2);
    }
}
