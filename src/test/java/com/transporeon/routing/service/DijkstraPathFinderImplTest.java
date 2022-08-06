package com.transporeon.routing.service;

import com.transporeon.routing.service.PathFinder.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;

class DijkstraPathFinderImplTest {

    private final PathFinder pathFinder = new DijkstraPathFinderImpl();

    @Test
    void shouldFindShortestPath() {
    }

    @Test
    void shouldNotFindShortestPath_GivenEmptyList() {
        assertThat(pathFinder.findShortestPath(emptyMap(),1,2,3).isEmpty()).isTrue();
    }

}