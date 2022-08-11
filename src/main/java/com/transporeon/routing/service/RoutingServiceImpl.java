package com.transporeon.routing.service;

import com.transporeon.routing.config.RoutingConfig;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.entity.Flight;
import com.transporeon.routing.model.Route;
import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.repository.FlightRepository;
import com.transporeon.routing.service.PathFinder.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class RoutingServiceImpl implements RoutingService {
    private final PathFinder pathFinder;
    private final Map<String, Airport> airports;
    private final Map<Airport, List<Node<Airport>>> airportFlights;
    private final RoutingConfig routingConfig;

    public RoutingServiceImpl(FlightRepository flightRepository,
                              AirportRepository airportRepository,
                              GroundRoutingService groundRoutingService,
                              PathFinder pathFinder,
                              RoutingConfig routingConfig) {
        this.routingConfig = routingConfig;
        this.pathFinder = pathFinder;
        airports = group(airportRepository.findAll());
        airportFlights = group(flightRepository.findAll(), airports);
        merge(airportFlights, getCloseAirports(groundRoutingService, routingConfig));
    }

    @Override
    public Optional<Route<Airport>> findRoute(String sourceAirport, String destAirport) {
        List<Airport> shortestPath = pathFinder.findShortestPath(airportFlights, airports.get(sourceAirport), airports.get(destAirport));
        if (shortestPath.isEmpty()) return Optional.empty();
        return Optional.of(toRoute(shortestPath));
    }

    private Map<Airport, List<Node<Airport>>> getCloseAirports(GroundRoutingService groundRoutingService, RoutingConfig routingConfig) {
        List<Airport> airportList = airports.values().stream().toList();
        return groundRoutingService.getCloseAirports(routingConfig.getGroundTransferThreshold(), airportList);
    }

    /**
     * Adding closeAirportEdges to regular airportFlights
     *
     * @param airportFlights    is mutated as a result of this function
     * @param closeAirportEdges close airports grouped by airport
     */
    private void merge(Map<Airport, List<Node<Airport>>> airportFlights, Map<Airport, List<Node<Airport>>> closeAirportEdges) {
        for (Map.Entry<Airport, List<Node<Airport>>> closeAirports : closeAirportEdges.entrySet()) {
            airportFlights.getOrDefault(closeAirports.getKey(), new ArrayList<>()).addAll(closeAirports.getValue());
        }
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
        Airport prev = shortestPath.get(0);
        Route<Airport> route = new Route<>(prev);
        for (int i = 1; i < shortestPath.size(); i++) {
            Airport curr = shortestPath.get(i);
            double edgeLength = prev.distanceTo(curr);
            if (edgeLength > routingConfig.getGroundTransferThreshold()) {
                route.add(curr);
            } else {
                route.addViaGround(curr);
            }
        }
        return route;
    }

}
