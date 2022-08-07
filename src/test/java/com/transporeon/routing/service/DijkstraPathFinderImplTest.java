package com.transporeon.routing.service;

import com.transporeon.routing.service.PathFinder.Node;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraPathFinderImplTest {

    private final PathFinder pathFinder = new DijkstraPathFinderImpl();

    @Test
    void shouldFindShortestPathGivenOnlyDirectEdge() {
        int source = 0;
        int dest = 2;
        int maxHops = 10;
        Map<Integer, List<Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new Node<>(1, 9), new Node<>(2, 6), new Node<>(3, 5), new Node<>(4, 3)),
                2, List.of(new Node<>(1, 2), new Node<>(3, 4))
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
        Map<Integer, List<Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new Node<>(1, 1), new Node<>(5, 10)),
                1, List.of(new Node<>(2, 1)),
                2, List.of(new Node<>(3, 1)),
                3, List.of(new Node<>(4, 1)),
                5, List.of(new Node<>(4, 20))
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
        Map<String, List<Node<String>>> adjacencyList = Map.of(
                "A", List.of(new Node<>("E", 1), new Node<>("D", 10)),
                "E", List.of(new Node<>("F", 1)),
                "F", List.of(new Node<>("G", 1)),
                "G", List.of(new Node<>("H", 1)),
                "H", List.of(new Node<>("B", 1)),
                "D", List.of(new Node<>("G", 1))
        );
        Optional<List<String>> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest, maxHops);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of("A","D","G","H","B"));
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
        assertThat(pathFinder.findShortestPath(emptyMap(), 1, 2, 3).isEmpty()).isTrue();
    }

}