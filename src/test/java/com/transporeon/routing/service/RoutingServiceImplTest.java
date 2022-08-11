package com.transporeon.routing.service;

import com.transporeon.routing.config.RoutingConfig;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.model.Route;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import com.transporeon.routing.repository.FlightRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Quick distance <a href="https://www.airmilescalculator.com/distance3/aer-to-kzn-led/">calculator</a>
 * that can be used for test data setup
 */
@Slf4j
public class RoutingServiceImplTest {
    private final Map<String, Airport> airports;
    private RoutingService routingService;
    private RoutingServiceImpl routingService2;
    private final AirportRepositoryImpl airportRepository = new AirportRepositoryImpl(getClass().getResourceAsStream("/airports.csv"));
    private final FlightRepositoryImpl flightRepository = new FlightRepositoryImpl(getClass().getResourceAsStream("/flights-trimmed.csv"));
    private final FlightRepositoryImpl flightRepository2 = new FlightRepositoryImpl(getClass().getResourceAsStream("/flights-trimmed-2.csv"));
    private final FlightRepositoryImpl flightRepository3 = new FlightRepositoryImpl(getClass().getResourceAsStream("/flights-trimmed-3.csv"));
    private final GroundRoutingService groundRoutingService = new RTreeRoutingService();
    private RoutingService routingService3;

    public RoutingServiceImplTest() {
        this.airports = group(airportRepository.findAll());//for more informative tests
    }

    @BeforeEach
    void setUp() {
        RoutingConfig config = new RoutingConfig(2, 100d);
        PathFinder pathFinder = new SmartPathFinder(config.getGroundTransferThreshold(), config.getMaxStops());
        routingService = new RoutingServiceImpl(flightRepository, airportRepository, groundRoutingService, pathFinder, config);
        routingService2 = new RoutingServiceImpl(flightRepository2, airportRepository, groundRoutingService, pathFinder, config);

        RoutingConfig config2 = new RoutingConfig(0, 101d);//distance from TLL to HEL
        PathFinder pathFinder2 = new SmartPathFinder(config2.getGroundTransferThreshold(), config2.getMaxStops());
        routingService3 = new RoutingServiceImpl(flightRepository3, airportRepository, groundRoutingService, pathFinder2, config2);
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
        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("KRR")).add(toAirport("LED"));//1,932.84 km
        Route<Airport> route = routingService.findRoute("AER", "LED").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);
    }

    @DisplayName("should not consider close HEL airport as an additional stop")
    @Test
    void shouldNotConsiderCloseAirportsAsAnAdditionalStop() {
        Route<Airport> expectedRoute = new Route<>(toAirport("TLL")).addViaGround(toAirport("HEL")).add(toAirport("TAY"));
        Route<Airport> route = routingService3.findRoute("TLL", "TAY").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);
    }

    @DisplayName("should use ground move to HEL from TLL even if there is no flight")
    @Test
    void shouldConsiderCloseAirportsEvenIfThereIsNoFlightThere() {
        Route<Airport> expectedRoute = new Route<>(toAirport("TLL")).addViaGround(toAirport("HEL")).add(toAirport("TAY"));
        Route<Airport> route = routingService3.findRoute("TLL", "TAY").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);
    }

    @DisplayName("should return shortest compound route when shorter flights removed from the database")
    @Test
    void shouldReturnCompoundRoute2Stops() {
        Route<Airport> expectedRoute = new Route<>(toAirport("AER")).add(toAirport("TZX")).add(toAirport("IST")).add(toAirport("LED"));//3,260.12 km
        Route<Airport> route = routingService2.findRoute("AER", "LED").orElse(null);
        assertThat(route).isEqualTo(expectedRoute);
    }

    private Airport toAirport(String code) {
        return airports.get(code);
    }

    private static Map<String, Airport> group(List<Airport> airports) {
        return airports.stream()
                .filter(airport -> StringUtils.isNotBlank(airport.getIataCode()))
                .collect(toMap(Airport::getIataCode, Function.identity(), (airport, airport2) -> airport));
    }

}
