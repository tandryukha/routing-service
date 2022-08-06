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

    @Test//todo parametrized test
    void shouldFindShortestPathGivenOnlyDirectEdge() {
        Optional<List<Integer>> shortestPath = pathFinder.findShortestPath(Map.of(
                0, List.of(new Node<>(1, 9), new Node<>(2, 6), new Node<>(3, 5), new Node<>(4, 3)),
                2, List.of(new Node<>(1, 2), new Node<>(3, 4))
        ), 0, 2, 3);
        assertThat(shortestPath.isEmpty()).isFalse();
        assertThat(shortestPath.get()).isEqualTo(List.of(0, 2));
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
        assertThat(pathFinder.findShortestPath(emptyMap(), 1, 2, 3).isEmpty()).isTrue();
    }

}