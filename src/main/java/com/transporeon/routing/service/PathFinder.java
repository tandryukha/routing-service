package com.transporeon.routing.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
