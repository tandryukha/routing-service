package com.transporeon.routing.service;

import java.util.List;
import java.util.Map;

public interface PathFinder {

    /**
     *
     * @param adjacencyList
     * @param source path starting point
     * @param dest path target point
     * @param maxHops Max number of hops from source to dest. For example, 2 hops == 1 intermediate node is allowed between source and dest
     * @return
     * @param <T>
     */
    <T> List<T> findShortestPath(Map<T, List<Node<T>>> adjacencyList, T source, T dest, int maxHops);

    record Node<T>(T value, double distance) implements Comparable<Node<T>> {
        @Override
        public int compareTo(Node<T> other) {
            return Double.compare(distance, other.distance());
        }
    }

}
