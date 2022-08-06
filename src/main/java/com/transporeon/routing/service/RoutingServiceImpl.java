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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class RoutingServiceImpl implements RoutingService {
    private final int maxStops;
    private final PathFinder pathFinder;
    private final Map<String, Airport> airports;
    Map<Airport, List<Node<Airport>>> airportFlights;

    public RoutingServiceImpl(FlightRepository flightRepository, AirportRepository airportRepository, int maxStops, PathFinder pathFinder) {
        airports = airportRepository.findAll().stream().collect(toMap(Airport::getIataCode, Function.identity()));
        List<Flight> flights = flightRepository.findAll();
        airportFlights = convert(flights, airports);
        this.maxStops = maxStops;
        this.pathFinder = pathFinder;
    }

    //thinking process...
    //what if you ignore dijkstra and start from sourceNode, recurse through all its edges to the number of maxStops.
    // Brute force recursion: The time complexity will be O(E+e^k) where e - avg num of edges per node, k - nodesBeingExamined(maxStops+1)
    // dijkstra algorithm time complexity is O(V+E*logV)
    // Given that in our trimmed database we have E=67K, V=3.5K, e=E/V=20, k=4
    // Dijkstra gives us O(3.5K+67K*log3.5K) = ~240K operations  vs brute force that will give us 67K + 20^4 = 230K
    // According, to my calculations above, we can say that both algorithms are of the same complexity given that amount of max stops stays low
    // but to make it future-proof, not just tailored to the current requirements, we'd better use Dijkstra.
    // At least it's not worse that brute-forse and can outperform brute-force given more stops
    @Override
    public Optional<Route> findRoute(String sourceAirport, String destAirport) {
        Optional<List<Airport>> shortestPath = pathFinder.findShortestPath(airportFlights, airports.get(sourceAirport), airports.get(destAirport), maxStops);
        if (shortestPath.isEmpty()) return Optional.empty();
        return Optional.of(toRoute(shortestPath.get()));
    }

    private Map<Airport, List<Node<Airport>>> convert(List<Flight> flights, Map<String, Airport> airports) {
        return flights.stream()
                .collect(groupingBy(flight -> airports.get(flight.getSourceAirport()),
                        mapping(flight -> toAirportNode(airports, flight), toList())));
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
