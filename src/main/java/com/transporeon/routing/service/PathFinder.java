package com.transporeon.routing.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PathFinder {

    <T> Optional<List<T>> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxStops);

    @Data
    @RequiredArgsConstructor
    class Node<T> implements Comparable<Node<T>> {
        private final T value;
        private final double distance;
        private int hopNumber;

        @Override
        public int compareTo(Node<T> other) {
            return Double.compare(distance, other.getDistance());
        }
    }

}
