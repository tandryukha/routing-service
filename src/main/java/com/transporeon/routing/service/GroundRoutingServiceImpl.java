package com.transporeon.routing.service;

import com.transporeon.routing.entity.Airport;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Uses brute-force approach
 */
public class GroundRoutingServiceImpl implements GroundRoutingService {
    @Override
    public Map<Airport, List<PathFinder.Node<Airport>>> getCloseAirports(double groundTransferThreshold, List<Airport> airports) {
        Map<Airport, List<PathFinder.Node<Airport>>> rawCloseAirports = airports.stream()
                .collect(toMap(airport -> airport, airport -> getCloseAirports(airport, airports, groundTransferThreshold)));
        Map<Airport, List<PathFinder.Node<Airport>>> nonEmptyCloseAirports = rawCloseAirports.entrySet().stream().filter(entry -> !entry.getValue().isEmpty()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return nonEmptyCloseAirports;
    }

    private List<PathFinder.Node<Airport>> getCloseAirports(Airport baseAirport, List<Airport> airports, double groundTransferThreshold) {
        return airports.stream()
                .filter(airport -> isClose(baseAirport, airport, groundTransferThreshold))
                .map(airport -> new PathFinder.Node<>(airport, baseAirport.distanceTo(airport)))
                .toList();
    }

    private boolean isClose(Airport airport1, Airport airport2, double groundTransferThreshold) {
        return airport1 != airport2 && airport1.distanceTo(airport2) <= groundTransferThreshold;
    }

}
