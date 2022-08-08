package com.transporeon.routing.service;

import com.transporeon.routing.Route;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.entity.Flight;
import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.repository.FlightRepository;
import com.transporeon.routing.service.PathFinder.Node;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class RoutingServiceImpl implements RoutingService {
    private final int maxStops;
    private final PathFinder pathFinder;
    private final Map<String, Airport> airports;
    Map<Airport, List<Node<Airport>>> airportFlights;

    public RoutingServiceImpl(FlightRepository flightRepository, AirportRepository airportRepository, int maxStops, PathFinder pathFinder) {
        airports = group(airportRepository.findAll());
        airportFlights = group(flightRepository.findAll(), airports);
        this.maxStops = maxStops;
        this.pathFinder = pathFinder;
    }

    @Override
    public Optional<Route<Airport>> findRoute(String sourceAirport, String destAirport) {
        List<Airport> shortestPath = pathFinder.findShortestPath(airportFlights, airports.get(sourceAirport), airports.get(destAirport), maxStops);
        if (shortestPath.isEmpty()) return Optional.empty();
        return Optional.of(toRoute(shortestPath));
    }

    private static Map<String, Airport> group(List<Airport> airports) {
        return airports.stream()
                .filter(RoutingServiceImpl::isValid)
                .collect(toMap(Airport::getIataCode, Function.identity(), (airport, airport2) -> airport));
    }

    private Map<Airport, List<Node<Airport>>> group(List<Flight> flights, Map<String, Airport> airports) {
        return flights.stream()
                .collect(groupingBy(flight -> airports.get(flight.getSourceAirport()),
                        mapping(flight -> toAirportNode(airports, flight), toList())));
    }

    private static boolean isValid(Airport airport) {
        return isNotBlank(airport.getIataCode());
    }

    private Node<Airport> toAirportNode(Map<String, Airport> airports, Flight flight) {
        Airport sourceAirport = airports.get(flight.getSourceAirport());
        Airport destAirport = airports.get(flight.getDestinationAirport());
        return new Node<>(destAirport, destAirport.distanceTo(sourceAirport));
    }

    private Route<Airport> toRoute(List<Airport> shortestPath) {
        Route<Airport> route = new Route<>(shortestPath.get(0));
        for (int i = 1; i < shortestPath.size(); i++) {
            route.add(shortestPath.get(i));
        }
        return route;
    }

}
