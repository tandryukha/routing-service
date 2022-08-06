package com.transporeon.routing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void shouldPrintRouteCorrectly() {
        Route route = new Route("AER").add("KZN").add("LED");
        assertThat(route.toString()).isEqualTo("AER->KZN->LED");
    }
}