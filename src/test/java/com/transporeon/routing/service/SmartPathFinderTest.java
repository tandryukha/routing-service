package com.transporeon.routing.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class SmartPathFinderTest {

    @Test
    void shouldFindShortestPathGivenOnlyDirectEdge() {
        int source = 0;
        int dest = 2;
        int maxHops = 9;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<Integer, List<PathFinder.Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new PathFinder.Node<>(1, 9), new PathFinder.Node<>(2, 6), new PathFinder.Node<>(3, 5), new PathFinder.Node<>(4, 3)),
                2, List.of(new PathFinder.Node<>(1, 2), new PathFinder.Node<>(3, 4))
        );
        List<Integer> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEqualTo(List.of(0, 2));
    }

    @Test
    void shouldReturnPathThatDoesntExceedHopLimit() {
        int source = 0;
        int dest = 4;
        int maxHops = 1;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<Integer, List<PathFinder.Node<Integer>>> adjacencyList = Map.of(
                0, List.of(new PathFinder.Node<>(1, 1), new PathFinder.Node<>(5, 10)),
                1, List.of(new PathFinder.Node<>(2, 1)),
                2, List.of(new PathFinder.Node<>(3, 1)),
                3, List.of(new PathFinder.Node<>(4, 1)),
                5, List.of(new PathFinder.Node<>(4, 20))
        );
        List<Integer> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEqualTo(List.of(0, 5, 4));
    }

    @Test
    void shouldReturnPathThatDoesntExceedHopLimit2() {
        String source = "A";
        String dest = "B";
        int maxHops = 3;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<String, List<PathFinder.Node<String>>> adjacencyList = Map.of(
                "A", List.of(new PathFinder.Node<>("E", 1), new PathFinder.Node<>("D", 10)),
                "E", List.of(new PathFinder.Node<>("F", 1)),
                "F", List.of(new PathFinder.Node<>("G", 1)),
                "G", List.of(new PathFinder.Node<>("H", 1)),
                "H", List.of(new PathFinder.Node<>("B", 1)),
                "D", List.of(new PathFinder.Node<>("G", 1))
        );
        List<String> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEqualTo(List.of("A", "D", "G", "H", "B"));
    }

    @Test
    void shouldNotReturnPathIfAllPathsExceedLimit() {
        String source = "A";
        String dest = "B";
        int maxHops = 2;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<String, List<PathFinder.Node<String>>> adjacencyList = Map.of(
                "A", List.of(new PathFinder.Node<>("E", 1), new PathFinder.Node<>("D", 10)),
                "E", List.of(new PathFinder.Node<>("F", 1)),
                "F", List.of(new PathFinder.Node<>("G", 1)),
                "G", List.of(new PathFinder.Node<>("H", 1)),
                "H", List.of(new PathFinder.Node<>("B", 1)),
                "D", List.of(new PathFinder.Node<>("G", 1))
        );
        List<String> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEmpty();
    }

    @DisplayName("Given close distance between H and B it should not be counted as a hop and can be included in shortest path")
    @Test
    void shouldNotCountHopBetweenCloseNodes() {
        String source = "A";
        String dest = "B";
        int maxHops = 2;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<String, List<PathFinder.Node<String>>> adjacencyList = Map.of(
                "A", List.of(new PathFinder.Node<>("E", 1), new PathFinder.Node<>("D", 10)),
                "E", List.of(new PathFinder.Node<>("F", 1)),
                "F", List.of(new PathFinder.Node<>("G", 1)),
                "G", List.of(new PathFinder.Node<>("H", 1)),
                "H", List.of(new PathFinder.Node<>("B", 0.001)),
                "D", List.of(new PathFinder.Node<>("G", 1))
        );
        List<String> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEqualTo(List.of("A", "D", "G", "H", "B"));
    }

    @DisplayName("Given two paths the shorter should be taken into account even if longer is between close nodes")
    @Test
    void shouldTakeDistanceBetweenCloseNodesIntoAccount() {
        String source = "A";
        String dest = "B";
        int maxHops = 0;
        PathFinder pathFinder = new SmartPathFinder(0.001d, maxHops);

        Map<String, List<PathFinder.Node<String>>> adjacencyList = Map.of(
                "A", List.of(new PathFinder.Node<>("B", 1), new PathFinder.Node<>("C", 0.998)),
                "C", List.of(new PathFinder.Node<>("D", 0.001)),
                "D", List.of(new PathFinder.Node<>("E", 0.001)),
                "E", List.of(new PathFinder.Node<>("B", 0.001))
        );
        List<String> shortestPath = pathFinder.findShortestPath(adjacencyList, source, dest);
        assertThat(shortestPath).isEqualTo(List.of("A", "B"));
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
        PathFinder pathFinder = new SmartPathFinder(0.001d, 3);
        assertThat(pathFinder.findShortestPath(emptyMap(), 1, 2).isEmpty()).isTrue();
    }

}