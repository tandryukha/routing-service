package com.transporeon.routing.service;

import java.util.*;

import static java.util.Collections.emptyList;

/**
 * Customized and tweaked to the interface Dijkstra shortest path algorithm
 */
@Deprecated(forRemoval = true,since = "doesn't allow to find shortest path when maxHops taken into account")
public class DijkstraPathFinderImpl implements PathFinder {

@Override
public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxHops) {//todo refactor //todo document params
    if (adjacencyList.isEmpty() || maxHops < 1) return Optional.empty();
    PriorityQueue<Node<T>> heap = new PriorityQueue<>();
    Map<T, Double> distance = new HashMap<>();
    Set<T> visited = new HashSet<>();//todo Another little optimisation is to use parent also for marking nodes as visited, so you don't actually need both parent and visited.
    Map<T, T> paths = new HashMap<>();
    paths.put(source, null);
    // Distance to the source is 0
    distance.put(source, 0d);
    // Add source node to the priority queue
    heap.add(new Node<>(source, 0));
    while (!heap.isEmpty()) {
        Node<T> current = heap.remove();// Picking the minimum distance node from the priority queue
        if (current.getValue().equals(dest)) break;//reached the destination
        int hopCount = current.getHopNumber();
        if (hopCount >= maxHops) continue;//current path reaches maxHopsLimit
        visited.add(current.getValue());
        for (Node<T> adjacent : adjacencyList.getOrDefault(current.getValue(), emptyList())) {
            if (visited.contains(adjacent.getValue())) continue;
            double distanceToAdjNode = adjacent.getDistance() + distance.get(current.getValue());
            // If current route is closer than what we already have
            if (distanceToAdjNode < distance.getOrDefault(adjacent.getValue(), Double.POSITIVE_INFINITY)) {
                distance.put(adjacent.getValue(), distanceToAdjNode);
                paths.put(adjacent.getValue(), current.getValue());
            }
            adjacent.setHopNumber(hopCount + 1);
            heap.add(adjacent);
        }
    }

    return getShortestPath(source, dest, paths);
}

    private static <T> Optional<List<T>> getShortestPath(T source, T dest, Map<T, T> paths) {
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
