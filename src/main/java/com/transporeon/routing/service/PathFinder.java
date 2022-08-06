package com.transporeon.routing.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PathFinder {

    <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops);

    record Node<T>(T value, double distance) {
    }

}
