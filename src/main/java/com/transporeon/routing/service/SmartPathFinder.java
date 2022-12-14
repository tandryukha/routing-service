package com.transporeon.routing.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

/**
 * Path search algorithm that uses Dynamic programming approach
 */
@Slf4j
@RequiredArgsConstructor
public class SmartPathFinder implements PathFinder {
    private final double hopThreshold;
    private final int maxStops;

    @Override
    public <T> List<T> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest) {
        if (adjacencyList.isEmpty() || maxStops < 0) return emptyList();
        Map<T, Map<Integer, List<Node<T>>>> lookupTable = new HashMap<>();
        List<Node<T>> result = findShortestPathCached(adjacencyList, source, dest, maxStops+1, lookupTable);
        return result.stream().map(Node::value).toList();
    }

    private <T> List<Node<T>> findShortestPathCached(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops, Map<T, Map<Integer, List<Node<T>>>> lookupTable) {
        if (hops < 0) return emptyList();//base case
        if (source == dest) return toLinkedList(new Node<>(dest, 0d));//base case

        List<Node<T>> lookupValue = Optional.ofNullable(lookupTable.get(source)).map(map -> map.get(hops)).orElse(null);
        if (nonNull(lookupValue)) return lookupValue;

        memoize(lookupTable, source, hops, emptyList());//mark current combination of source+hops as visited to avoid loop
        List<Node<T>> shortestPath = findShortestPath(adjacencyList, source, dest, hops, lookupTable);
        memoize(lookupTable, source, hops, shortestPath);
        return shortestPath;
    }

    private <T> List<Node<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops, Map<T, Map<Integer, List<Node<T>>>> lookupTable) {
        List<Node<T>> adjacentNodes = adjacencyList.getOrDefault(source, emptyList());
        if (adjacentNodes.isEmpty()) return emptyList();
        double minDistanceToDest = Double.POSITIVE_INFINITY;
        double distanceToAdjacentNode = Double.POSITIVE_INFINITY;
        List<Node<T>> shortestPath = emptyList();
        for (Node<T> adjacentNode : adjacentNodes) {
            int hopsLeft = isHop(adjacentNode) ? hops - 1 : hops;
            List<Node<T>> path = findShortestPathCached(adjacencyList, adjacentNode.value(), dest, hopsLeft, lookupTable);
            if (path.isEmpty()) continue;
            double distanceToDest = adjacentNode.distance() + getDistance(path);
            if (distanceToDest < minDistanceToDest) {
                minDistanceToDest = distanceToDest;
                shortestPath = path;
                distanceToAdjacentNode = adjacentNode.distance();
            }
        }
        if (!shortestPath.isEmpty()) {
            shortestPath.add(0, new Node<>(source, distanceToAdjacentNode));
        }
        return shortestPath;
    }

    private <T> boolean isHop(Node<T> adjacentNode) {
        return adjacentNode.distance() > hopThreshold;
    }

    private <T> void memoize(Map<T, Map<Integer, List<Node<T>>>> lookupTable, T source, int hops, List<Node<T>> shortestPath) {
        Map<Integer, List<Node<T>>> subLookupTable = lookupTable.getOrDefault(source, new HashMap<>());
        subLookupTable.put(hops, shortestPath);
        lookupTable.put(source, subLookupTable);
    }

    private <T> List<Node<T>> toLinkedList(Node<T> node) {
        LinkedList<Node<T>> nodes = new LinkedList<>();
        nodes.add(node);
        return nodes;
    }

    /**
     * Compute distance along the path
     */
    private <T> double getDistance(List<Node<T>> path) {
        return path.stream().map(Node::distance).reduce(Double::sum).orElse(Double.NEGATIVE_INFINITY);
    }
}
