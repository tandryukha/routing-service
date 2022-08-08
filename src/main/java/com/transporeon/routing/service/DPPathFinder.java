package com.transporeon.routing.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Path finder using Dynamic programming approach
 */
public class DPPathFinder implements PathFinder {
    @Override
    public <T> List<T> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxHops) {
        if (adjacencyList.isEmpty() || maxHops < 1) return emptyList();
        List<Node<T>> result = findShortestPathCached(adjacencyList, source, dest, maxHops, new HashMap<>());
        return result.stream().map(Node::getValue).toList();
    }

    private <T> List<Node<T>> findShortestPathCached(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops, Map<T, Map<Integer, List<Node<T>>>> lookupTable) {
        if (source == dest) return toLinkedList(new Node<>(dest, 0d));//base case
        if (hops == 0) return emptyList();//base case

        List<Node<T>> lookupValue = lookupTable.getOrDefault(source, emptyMap()).get(hops);
        if (isNotEmpty(lookupValue)) return lookupValue;

        List<Node<T>> shortestPath = findShortestPath(adjacencyList, source, dest, hops, lookupTable);
        memoize(lookupTable, source, hops, shortestPath);
        return shortestPath;
    }

    private <T> List<Node<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops, Map<T, Map<Integer, List<Node<T>>>> lookupTable) {
        List<Node<T>> adjacentNodes = adjacencyList.getOrDefault(source, emptyList());
        if (adjacentNodes.isEmpty()) return emptyList();
        double minDistanceToDest = Double.POSITIVE_INFINITY;
        List<Node<T>> shortestPath = emptyList();
        double distanceToAdjacentNode = Double.POSITIVE_INFINITY;
        for (Node<T> adjacentNode : adjacentNodes) {
            List<Node<T>> path = findShortestPathCached(adjacencyList, adjacentNode.getValue(), dest, hops - 1, lookupTable);
            if (path.isEmpty()) continue;
            double distanceToDest = getDistance(path);
            if (distanceToDest < minDistanceToDest) {
                minDistanceToDest = distanceToDest;
                shortestPath = path;
                distanceToAdjacentNode = adjacentNode.getDistance();
            }
        }
        if (!shortestPath.isEmpty()) {
            shortestPath.add(0, new Node<>(source, distanceToAdjacentNode));
        }
        return shortestPath;
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

    private <T> double getDistance(List<Node<T>> path) {
        return path.stream().map(Node::getDistance).reduce(Double::sum).orElse(Double.NEGATIVE_INFINITY);
    }
}
