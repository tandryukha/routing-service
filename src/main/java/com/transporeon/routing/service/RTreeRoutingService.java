package com.transporeon.routing.service;

import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.external.GeoLocation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

/**
 * Uses brute-force approach
 */
@Slf4j
public class RTreeRoutingService implements GroundRoutingService {
    private static final double EARTH_RADIUS = 6371.01;
    private final ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);

    @Override
    public Map<Airport, List<PathFinder.Node<Airport>>> getCloseAirports(double groundTransferThreshold, List<Airport> airports) {
        List<Entry<Airport, Point>> geometryList = airports.stream().map(airport -> {
            String[] coords = airport.getCoordinates().split(",");
            double longitude = Double.parseDouble(coords[0]);
            double latitude = Double.parseDouble(coords[1]);
            return Entries.entry(airport, Geometries.pointGeographic(longitude, latitude));
        }).collect(Collectors.toList());
        RTree<Airport, Point> rtree = RTree.star().minChildren(10).maxChildren(100).create(geometryList);

        Map<Airport, List<PathFinder.Node<Airport>>> rawCloseAirports = airports.stream()
                .collect(toMap(airport -> airport, airport -> getCloseAirports(airport, rtree, groundTransferThreshold)));
        Map<Airport, List<PathFinder.Node<Airport>>> nonEmptyCloseAirports = rawCloseAirports.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return nonEmptyCloseAirports;
    }

    private List<PathFinder.Node<Airport>> getCloseAirports(Airport airport, RTree<Airport, Point> rtree, double distance) {
        String[] coords = airport.getCoordinates().split(",");
        double longitude = Double.parseDouble(coords[0]);
        double latitude = Double.parseDouble(coords[1]);
        GeoLocation airportGeo = GeoLocation.fromDegrees(latitude, longitude);
        Rectangle closeAirportsRectangle = getBoundingRectangle(airportGeo, distance);
        log.info("[{}] Searching for airports close to {}", counter.get(), airport);
        counter.set(counter.get() + 1);
        Iterable<Entry<Airport, Point>> entries = rtree.search(closeAirportsRectangle).toBlocking().toIterable();//todo add logging with iterations counter to understand when it fails
        log.info("Found {} airports close to {}", StreamSupport.stream(entries.spliterator(), false).count(), airport);
        return StreamSupport.stream(entries.spliterator(), false)
                .filter(e -> e.value() != airport)
                .map(e -> new PathFinder.Node<>(e.value(), airport.distanceTo(e.value())))
                .toList();
    }

    private Rectangle getBoundingRectangle(GeoLocation location, double distance) {
        GeoLocation[] boundingCoords = location.boundingCoordinates(distance, EARTH_RADIUS);
        double lat1 = boundingCoords[0].getLatitudeInDegrees();
        double lon1 = boundingCoords[0].getLongitudeInDegrees();
        double lat2 = boundingCoords[1].getLatitudeInDegrees();
        double lon2 = boundingCoords[1].getLongitudeInDegrees();
        return Geometries.rectangleGeographic(lon1, lat1, lon2, lat2);
    }


}
