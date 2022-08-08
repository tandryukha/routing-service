package com.transporeon.routing.service;

import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.model.Route;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import com.transporeon.routing.repository.FlightRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Quick distance <a href="https://www.airmilescalculator.com/distance3/aer-to-kzn-led/">calculator</a>
 * that can be used for test data setup
 */
@Slf4j
public class RoutingServiceImplTest {

    private RoutingService routingService;
    private final PathFinder pathFinder = new DPPathFinder();
    private final AirportRepositoryImpl airportRepository = new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv"));
    private final FlightRepositoryImpl flightRepository = new FlightRepositoryImpl(Path.of("src/test/resources/flights-trimmed.csv"));

    @BeforeEach
    void setUp() {
        routingService = new RoutingServiceImpl(flightRepository, airportRepository, 3, pathFinder);
    }

    @Test
    void shouldReturnDirectRoute() {
        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KZN"));
        Route<Airport> route = routingService.findRoute("AER", "KZN").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);
    }

    @DisplayName("should return shortest compound route when direct flight AER->LED deleted from database")
    @Test
    void shouldReturnCompoundRoute1Stop() {
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("TAS")).add(toAirport("LED"));//5788 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("TZX").add(toAirport("IST")).add(toAirport("LED"));//3266 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("OMS")).add(toAirport("LED"));//5307 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("EVN")).add(toAirport("LED"));//2918 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("IST")).add(toAirport("LED"));//3020 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("SVX")).add(toAirport("LED"));//3891 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KRR")).add(toAirport("LED"));//1935 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KIV")).add(toAirport("LED"));//2383 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KZN")).add(toAirport("LED"));//2729 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("MSQ")).add(toAirport("LED"));//2124 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("DME")).add(toAirport("LED"));//2007 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("VKO")).add(toAirport("LED"));//1991 km
//        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("SVO")).add(toAirport("LED"));//2003 km

        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("VKO")).add(toAirport("LED"));//2005 km
        Route<Airport> route = routingService.findRoute("AER", "LED").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);

    }


    private Airport toAirport(String code) {
        return Airport.builder().coordinates("0,0").iataCode(code).build();
    }

}
