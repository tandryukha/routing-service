package com.transporeon.routing.service;

import java.util.List;
import java.util.Map;

public interface PathFinder {

    /**
     *
     * @param adjacencyList input graph
     * @param source path starting point
     * @param dest path target point
     * @return shortest path
     */
    <T> List<T> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest);

    record Node<T>(T value, double distance) implements Comparable<Node<T>> {
        @Override
        public int compareTo(Node<T> other) {
            return Double.compare(distance, other.distance());
        }
    }

}
