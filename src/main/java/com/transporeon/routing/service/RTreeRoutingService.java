package com.transporeon.routing.service;

import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.entity.GeoLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

/**
 * Uses R*-tree data structure to index airports by geo coordinates and bounding rectangle to search close airports in the R-tree
 */
@Service
public class RTreeRoutingService implements GroundRoutingService {
    private static final double EARTH_RADIUS = 6371.01;

    @Override
    public Map<Airport, List<PathFinder.Node<Airport>>> getCloseAirports(double groundTransferThreshold, List<Airport> airports) {
        List<Entry<Airport, Point>> geometryList = airports.stream().map(airport -> {
            GeoLocation geoLocation = airport.getGeoLocation();
            return Entries.entry(airport, Geometries.pointGeographic(geoLocation.getLongitudeInDegrees(), geoLocation.getLatitudeInDegrees()));
        }).collect(Collectors.toList());
        RTree<Airport, Point> rtree = createRTree(geometryList);
        return airports.stream()
                .collect(toMap(airport -> airport, airport -> getCloseAirports(airport, rtree, groundTransferThreshold))).entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static RTree<Airport, Point> createRTree(List<Entry<Airport, Point>> geometryList) {
        return RTree.star().minChildren(10).maxChildren(100).create(geometryList);
    }

    private List<PathFinder.Node<Airport>> getCloseAirports(Airport airport, RTree<Airport, Point> rtree, double distance) {
        Rectangle closeAirportsRectangle = getBoundingRectangle(airport.getGeoLocation(), distance);
        Iterable<Entry<Airport, Point>> entries = rtree.search(closeAirportsRectangle).toBlocking().toIterable();
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
