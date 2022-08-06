package com.transporeon.routing.service;

import com.transporeon.routing.service.PathFinder.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraPathFinderImplTest {

    private PathFinder pathFinder = new DijkstraPathFinderImpl();

    @Test
    void shouldFindShortestPath() {
//        assertThat(pathFinder.findShortestPath(getAdjacencyList(), "ABC", "XYZ", 1).isEmpty()).isTrue();
    }

    private List<List<Node<String>>> getAdjacencyList() {
//        List.of(
//                List.of(new Node("ABC"))
//        )
        return null;
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
//        assertThat(pathFinder.findShortestPath(emptyList(), "ABC", "XYZ", 10).isEmpty()).isTrue();
    }

}