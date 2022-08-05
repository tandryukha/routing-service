package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class RoutingServiceImplTest {


    private RoutingService routingService;

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl(new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv")));
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
        routingService = new RoutingServiceImpl(new AirportRepositoryImpl(Path.of("src/test/resources/airports-trimmed-1.csv")));


    }

}
