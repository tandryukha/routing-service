package com.transporeon.routing.service;

import com.transporeon.routing.entity.Airport;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RTreeRoutingServiceTest {

    private final GroundRoutingService groundRoutingService = new RTreeRoutingService();

    @Test
    void shouldReturnCloseAirports() {
        Airport tllAirport = Airport.builder().coordinates("24.832799911499997, 59.41329956049999").iataCode("TLL").build();
        Airport helAirport = Airport.builder().coordinates("24.963300704956, 60.317199707031").iataCode("HEL").build();
        Airport hrkAirport = Airport.builder().coordinates("36.290000915527344, 49.924800872802734").iataCode("HRK").build();
        Airport kbpAirport = Airport.builder().coordinates("30.894699096679688, 50.345001220703125").iataCode("KBP").build();
        List<Airport> airports = List.of(tllAirport, helAirport, hrkAirport, kbpAirport);
        Map<Airport, List<PathFinder.Node<Airport>>> expectedCloseAirports = Map.of(
                tllAirport,List.of(new PathFinder.Node<>(helAirport,100.77272598507493)),
                helAirport,List.of(new PathFinder.Node<>(tllAirport,100.77272598507493))
        );

        Map<Airport, List<PathFinder.Node<Airport>>> closeAirports = groundRoutingService.getCloseAirports(101d, airports);
        assertThat(closeAirports).isEqualTo(expectedCloseAirports);
    }
}