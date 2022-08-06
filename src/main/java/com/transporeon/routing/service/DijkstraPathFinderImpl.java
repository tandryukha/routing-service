package com.transporeon.routing.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DijkstraPathFinderImpl implements PathFinder {

    @Override
    public <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops) {
        return Optional.empty();
    }
}
