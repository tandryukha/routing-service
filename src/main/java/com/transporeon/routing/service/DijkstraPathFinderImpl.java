package com.transporeon.routing.service;

import java.util.*;

/**
 * Customized and tweaked to the interface Dijkstra shortest path algorithm
 */
public class DijkstraPathFinderImpl implements PathFinder {

//todo handle when there is no path
    //todo stop when destination reached
    //todo stop when maxStops reached
@Override
public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops) {
    PriorityQueue<Node<T>> heap = new PriorityQueue<>(adjacencyList.size());
    HashMap<T, Double> distance = new HashMap<>(adjacencyList.size());//todo store in node
    Set<T> visited = new HashSet<>(adjacencyList.size());
    HashMap<T, T> shortestPath = new HashMap<>(adjacencyList.size());
    shortestPath.put(source, null);
    // Add source node to the priority queue
    heap.add(new Node<>(source, 0));

    // Distance to the source is 0
    distance.put(source, 0d);
    while (visited.size() != adjacencyList.size() && !heap.isEmpty()) {
        // Removing the minimum distance node from the priority queue
        Node<T> closestNode = heap.remove();
        visited.add(closestNode.value());//todo store visited in node itself
        for (Node<T> node : adjacencyList.get(closestNode.value())) {
            if (visited.contains(node.value())) continue;
            double distanceToAdjNode = node.distance() + distance.get(closestNode.value());
            // If current route is closer than what we already have
            if (distanceToAdjNode < distance.getOrDefault(node.value(), Double.POSITIVE_INFINITY)) {
                distance.put(node.value(), distanceToAdjNode);
                shortestPath.put(node.value(), closestNode.value());//todo store in node
            }
            heap.add(node);
        }
    }

    //todo result

}
}
