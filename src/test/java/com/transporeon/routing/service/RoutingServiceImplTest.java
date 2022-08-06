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
    void shouldReturnDirectRoute() {
        Route expectedRoute = new Route("AER").add("KZN");
        Route route = routingService.findRoute("AER", "KZN");
        assertEquals(expectedRoute, route);
    }

    @Test
    void shouldReturnCompoundRoute1Stop() {
        Route expectedRoute = new Route("AER").add("KZN").add("LED");
        Route route = routingService.findRoute("AER", "LED");
        assertEquals(expectedRoute, route);
    }

}
