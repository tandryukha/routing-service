package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.repository.FlightRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoutingServiceImplTest {


    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl(new FlightRepositoryImpl(Path.of("src/main/resources/flights.csv")));
    }

    @Test
    void shouldReturnRoute() {
        String iataCode1 = "00A", iataCode2 = "RJX7";
        Route expectedRoute = new Route(iataCode1).add(iataCode2);
        Route route = routingService.findRoute(iataCode1, iataCode2);
        assertEquals(expectedRoute, route);
    }

    @Test
    void shouldReturnSmartRoute() {
        routingService = new RoutingServiceImpl(new FlightRepositoryImpl(Path.of("src/test/resources/airports-trimmed-1.csv")));


    }

}
