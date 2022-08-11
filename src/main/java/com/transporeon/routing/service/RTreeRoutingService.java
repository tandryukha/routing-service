package com.transporeon.routing.service;

import com.github.davidmoten.rtree.Entries;
import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;
import com.github.davidmoten.rtree.geometry.Rectangle;
import com.transporeon.routing.entity.Airport;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Uses brute-force approach
 */
public class RTreeRoutingService implements GroundRoutingService {
    @Override
    public Map<Airport, List<PathFinder.Node<Airport>>> getCloseAirports(double groundTransferThreshold, List<Airport> airports) {
        List<Entry<Airport, Point>> geometryList = airports.stream().map(airport -> {
            String[] coords = airport.getCoordinates().split(",");
            double longitude = Double.parseDouble(coords[0]);
            double latitude = Double.parseDouble(coords[1]);
            return Entries.entry(airport, Geometries.pointGeographic(longitude, latitude));
        }).toList();
        RTree<Airport, Point> rtree = RTree.create(geometryList);

        Map<Airport, List<PathFinder.Node<Airport>>> rawCloseAirports = airports.stream()
                .collect(toMap(airport -> airport, airport -> getCloseAirports(airport, rtree, groundTransferThreshold)));
        Map<Airport, List<PathFinder.Node<Airport>>> nonEmptyCloseAirports = rawCloseAirports.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return nonEmptyCloseAirports;


            //try circle with the radius 100km and then searching overlaps

        //or search for items within a distance from the given geometry:
        //
        //Observable<Entry<T, Geometry>> results =
        //    tree.search(Geometries.rectangle(0,0,2,2),5.0);


        //Search with a custom geometry and maxDistance
        //
        //As per the example above to do a proximity search you need to specify how to calculate distance between the geometry you are searching and the entry geometries:
        //
        //RTree<String, Point> tree = RTree.create();
        //Func2<Point, Polygon, Boolean> distancePointToPolygon = ...
        //Polygon polygon = ...
        //...
    }

    private List<PathFinder.Node<Airport>> getCloseAirports(Airport airport, RTree<Airport, Point> rtree, double groundTransferThreshold) {
        String[] coords = airport.getCoordinates().split(",");
        double longitude = Double.parseDouble(coords[0]);
        double latitude = Double.parseDouble(coords[1]);
        Point airportGeoPoint = Geometries.pointGeographic(longitude, latitude);
        Rectangle closeAirportsRectangle = Geometries.rectangleGeographic();
        Iterable<Entry<Airport, Point>> entries = rtree.search(closeAirportsRectangle).toBlocking().toIterable();
        //todo
    }


}
