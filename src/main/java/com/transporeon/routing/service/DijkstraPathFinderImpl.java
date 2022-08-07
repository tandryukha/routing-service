package com.transporeon.routing.service;

import java.util.*;

import static java.util.Collections.emptyList;

/**
 * Customized and tweaked to the interface Dijkstra shortest path algorithm
 */
public class DijkstraPathFinderImpl implements PathFinder {

    //todo stop when destination reached
    //todo stop when maxStops reached
@Override
public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops) {
    if (adjacencyList.isEmpty()) return Optional.empty();
    PriorityQueue<Node<T>> heap = new PriorityQueue<>(adjacencyList.size());
    HashMap<T, Double> distance = new HashMap<>(adjacencyList.size());
    Set<T> visited = new HashSet<>(adjacencyList.size());
    HashMap<T, T> paths = new HashMap<>(adjacencyList.size());
    paths.put(source, null);
    // Distance to the source is 0
    distance.put(source, 0d);
    // Add source node to the priority queue
    heap.add(new Node<>(source, 0));
    while (!heap.isEmpty()) {
        // Picking the minimum distance node from the priority queue
        Node<T> closestNode = heap.remove();
        visited.add(closestNode.value());
        for (Node<T> node : adjacencyList.getOrDefault(closestNode.value(), emptyList())) {
            if (visited.contains(node.value())) continue;
            double distanceToAdjNode = node.distance() + distance.get(closestNode.value());
            // If current route is closer than what we already have
            if (distanceToAdjNode < distance.getOrDefault(node.value(), Double.POSITIVE_INFINITY)) {
                distance.put(node.value(), distanceToAdjNode);
                paths.put(node.value(), closestNode.value());
            }
            heap.add(node);
        }
    }

    return getShortestPath(source, dest, paths);
}

    private static <T> Optional<List<T>> getShortestPath(T source, T dest, HashMap<T, T> paths) {
        if (!paths.containsKey(dest)) return Optional.empty();
        LinkedList<T> shortestPath = new LinkedList<>();
        shortestPath.add(dest);
        T curr = dest;
        while (curr != source) {
            T prevNode = paths.get(curr);
            shortestPath.addFirst(prevNode);
            curr = prevNode;
        }
        return Optional.of(shortestPath);
    }
}
