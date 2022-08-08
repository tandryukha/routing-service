package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.entity.Airport;
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
    private final PathFinder pathFinder = new DPPathFinder();

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl(new FlightRepositoryImpl(Path.of("src/main/resources/flights.csv")), new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv")), 3, pathFinder);
    }

    @Test
    void shouldReturnDirectRoute() {
        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KZN"));
        Optional<Route<Airport>> route = routingService.findRoute("AER", "KZN");
        assertEquals(expectedRoute, route.orElse(null));
    }

    @Test
    void shouldReturnCompoundRoute1Stop() {
        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KZN")).add(toAirport("LED"));
        Optional<Route<Airport>> route = routingService.findRoute("AER", "LED");
        assertEquals(expectedRoute, route.orElse(null));
    }

    private Airport toAirport(String code) {
        return Airport.builder().iataCode(code).build();
    }

}
