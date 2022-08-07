package com.transporeon.routing.service;

import com.transporeon.routing.service.PathFinder.Node;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraPathFinderImplTest {

    private final PathFinder pathFinder = new DijkstraPathFinderImpl();

    //todo parametrized tests

    @Test
    void shouldFindShortestPathGivenOnlyDirectEdge() {
        int source = 0;
        int dest = 2;
        int maxHops = 1;
        Map<Integer, List<Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new Node<>(1, 9), new Node<>(2, 6), new Node<>(3, 5), new Node<>(4, 3)),
                2, List.of(new Node<>(1, 2), new Node<>(3, 4))
        );
        Optional<List<Integer>> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest, maxHops);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of(0, 2));
    }

    @Test
    void shouldReturnPathThatDoesntExceedStopLimit() {
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
    void shouldNotFindShortestPath_GivenEmptyList() {
        assertThat(pathFinder.findShortestPath(emptyMap(), 1, 2, 3).isEmpty()).isTrue();
    }

}