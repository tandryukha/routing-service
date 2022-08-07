package com.transporeon.routing.service;

import java.util.*;

import static java.util.Collections.emptyList;

public class DPPathFinder implements PathFinder {
    @Override
    public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxHops) {
        //todo edge case handling can be copied from DijkstraPathFinder
        List<T> result = findShortestPathRec(adjacencyList, source, dest, maxHops);
        return Optional.empty();
    }

    private <T> List<T> findShortestPathRec(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops) {
        if (source == dest) return List.of(source);//base case
        if (hops == 0) return emptyList();//base case


        List<Node<T>> adjacentNodes = adjacencyList.get(source);
        double minDistance = Double.POSITIVE_INFINITY;
        List<T> shortestPath = emptyList();
        for (Node<T> adjacentNode : adjacentNodes) {
            List<T> path = findShortestPathRec(adjacencyList, adjacentNode.getValue(), dest, hops - 1);
            if (path.isEmpty()) continue;
            double distance = getDistance(path);
            if (distance < minDistance) {
                minDistance = distance;
                shortestPath = path;
            }
        }
        if (shortestPath.isEmpty()) return emptyList();
        shortestPath.add(0, source);
        return new LinkedList<>(shortestPath);
    }

    private <T> double getDistance(List<T> path) {
        //todo
    }
}
