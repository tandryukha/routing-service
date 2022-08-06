package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import com.transporeon.routing.repository.FlightRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoutingServiceImplTest {

    private RoutingService routingService;
    private final PathFinder pathFinder = new DijkstraPathFinderImpl();

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl(new FlightRepositoryImpl(Path.of("src/main/resources/flights.csv")), new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv")), 3, pathFinder);
    }

    @Test
    void shouldReturnDirectRoute() {
        Route expectedRoute = new Route("AER").add("KZN");
        Optional<Route> route = routingService.findRoute("AER", "KZN");
        assertEquals(expectedRoute, route.orElse(null));
    }

    @Test
    void shouldReturnCompoundRoute1Stop() {
        Route expectedRoute = new Route("AER").add("KZN").add("LED");
        Optional<Route> route = routingService.findRoute("AER", "LED");
        assertEquals(expectedRoute, route.orElse(null));
    }

}
