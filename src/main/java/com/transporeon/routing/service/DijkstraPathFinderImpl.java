package com.transporeon.routing.service;

import java.util.*;

/**
 * Customized and tweaked to the interface Dijkstra shortest path algorithm
 */
public class DijkstraPathFinderImpl implements PathFinder {

    @Override
    public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops) {
        PriorityQueue<Node<T>> queue = new PriorityQueue<>(adjacencyList.size());
        HashMap<T, Double> distance = new HashMap<>(adjacencyList.size());
        Set<T> visited = new HashSet<>(adjacencyList.size());
        // Add source node to the priority queue
        queue.add(new Node<>(source, 0));
        // Distance to the source is 0
        distance.put(source, 0d);
        while (visited.size() != adjacencyList.size() && !queue.isEmpty()) {
            // Removing the minimum distance node from the priority queue
            Node<T> closestNode = queue.remove();
            visited.add(closestNode.value());
            for (Node<T> adjacentNode : adjacencyList.get(closestNode.value())) {
                if (visited.contains(adjacentNode.value())) continue;
                double distanceToAdjNode = adjacentNode.distance() + distance.get(closestNode.value());
                // If current route is closer than what we already have
                if (distanceToAdjNode < distance.getOrDefault(adjacentNode.value(), Double.MAX_VALUE)) {
                    distance.put(adjacentNode.value(), distanceToAdjNode);
                }
                queue.add(adjacentNode);
            }
        }

        //todo result

    }
}
