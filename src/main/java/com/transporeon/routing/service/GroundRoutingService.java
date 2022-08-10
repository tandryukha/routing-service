package com.transporeon.routing.service;

import com.transporeon.routing.entity.Airport;

import java.util.List;
import java.util.Map;

public interface GroundRoutingService {
    Map<Airport, List<PathFinder.Node<Airport>>> getCloseAirports(double groundTransferThreshold, List<Airport> airports);
}
