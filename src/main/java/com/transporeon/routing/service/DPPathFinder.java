package com.transporeon.routing.service;

import java.util.*;

import static java.util.Collections.emptyList;

public class DPPathFinder implements PathFinder {
    @Override//todo avoid optional in contract - use empty list if shortest path not found
    public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxHops) {
        if (adjacencyList.isEmpty() || maxHops < 1) return Optional.empty();
        List<Node<T>> result = findShortestPathRec(adjacencyList, source, dest, maxHops);
        if (result.isEmpty()) return Optional.empty();
        return Optional.of(result.stream().map(Node::getValue).toList());
    }

    //todo is it possible to memoize results? take a look at the knapsack problem
    private <T> List<Node<T>> findShortestPathRec(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int hops) {
        if (source == dest) return toLinkedList(new Node<>(dest, 0d));//base case
        if (hops == 0) return emptyList();//base case

        List<Node<T>> adjacentNodes = adjacencyList.getOrDefault(source,emptyList());
        double minDistanceToDest = Double.POSITIVE_INFINITY;
        List<Node<T>> shortestPath = emptyList();
        double distanceToAdjacentNode = Double.POSITIVE_INFINITY;
        for (Node<T> adjacentNode : adjacentNodes) {
            List<Node<T>> path = findShortestPathRec(adjacencyList, adjacentNode.getValue(), dest, hops - 1);
            if (path.isEmpty()) continue;
            double distanceToDest = getDistance(path);
            if (distanceToDest < minDistanceToDest) {
                minDistanceToDest = distanceToDest;
                shortestPath = path;
                distanceToAdjacentNode = adjacentNode.getDistance();
            }
        }
        if (shortestPath.isEmpty()) return emptyList();
        shortestPath.add(0, new Node<>(source, distanceToAdjacentNode));
        return new LinkedList<>(shortestPath);
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
