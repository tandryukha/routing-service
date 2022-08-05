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
    void shouldReturnSimpleRoute() {
        String iataCode1 = "AER", iataCode2 = "KZN";
        Route expectedRoute = new Route(iataCode1).add(iataCode2);
        Route route = routingService.findRoute(iataCode1, iataCode2);
        assertEquals(expectedRoute, route);
    }

    @Test
    void shouldReturnCompoundRoute() {
        String iataCode1 = "AER", iataCode2 = "LED";
        Route expectedRoute = new Route(iataCode1).add("KZN").add(iataCode2);
        Route route = routingService.findRoute(iataCode1, iataCode2);
        assertEquals(expectedRoute, route);
    }

    @Test
    void shouldReturnSmartRoute() {
        routingService = new RoutingServiceImpl(new FlightRepositoryImpl(Path.of("src/test/resources/airports-trimmed-1.csv")));


    }

}
