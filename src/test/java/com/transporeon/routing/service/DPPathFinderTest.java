package com.transporeon.routing.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class DPPathFinderTest {

    private final PathFinder pathFinder = new DPPathFinder();

    @Test
    void shouldFindShortestPathGivenOnlyDirectEdge() {
        int source = 0;
        int dest = 2;
        int maxHops = 10;
        Map<Integer, List<PathFinder.Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new PathFinder.Node<>(1, 9), new PathFinder.Node<>(2, 6), new PathFinder.Node<>(3, 5), new PathFinder.Node<>(4, 3)),
                2, List.of(new PathFinder.Node<>(1, 2), new PathFinder.Node<>(3, 4))
        );
        Optional<List<Integer>> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest, maxHops);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of(0, 2));
    }

    @Test
    void shouldReturnPathThatDoesntExceedHopLimit() {
        int source = 0;
        int dest = 4;
        int maxHops = 2;//2 hops = 1 intermediate stop
        Map<Integer, List<PathFinder.Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new PathFinder.Node<>(1, 1), new PathFinder.Node<>(5, 10)),
                1, List.of(new PathFinder.Node<>(2, 1)),
                2, List.of(new PathFinder.Node<>(3, 1)),
                3, List.of(new PathFinder.Node<>(4, 1)),
                5, List.of(new PathFinder.Node<>(4, 20))
        );
        Optional<List<Integer>> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest, maxHops);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of(0, 5, 4));
    }

    @Test
    void shouldReturnPathThatDoesntExceedHopLimit2() {
        String source = "A";
        String dest = "B";
        int maxHops = 4;//4 hops = 3 intermediate stops
        Map<String, List<PathFinder.Node<String>>> adjacencyList = Map.of(
                "A", List.of(new PathFinder.Node<>("E", 1), new PathFinder.Node<>("D", 10)),
                "E", List.of(new PathFinder.Node<>("F", 1)),
                "F", List.of(new PathFinder.Node<>("G", 1)),
                "G", List.of(new PathFinder.Node<>("H", 1)),
                "H", List.of(new PathFinder.Node<>("B", 1)),
                "D", List.of(new PathFinder.Node<>("G", 1))
        );
        Optional<List<String>> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest, maxHops);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of("A", "D", "G", "H", "B"));
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
        assertThat(pathFinder.findShortestPath(emptyMap(), 1, 2, 3).isEmpty()).isTrue();
    }

}