package com.transporeon.routing;

import com.transporeon.routing.entity.Airport;
import com.transporeon.routing.model.Route;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RouteTest {

    @Test
    void shouldPrintRouteCorrectly() {
        Route<Airport> route = new Route<>(toAirport("AER", "24.832799911499997, 59.41329956049999"))
                .add(toAirport("KZN", "36.290000915527344, 49.924800872802734"))
                .add(toAirport("LED", "24.832799911499997, 59.41329956049999"));
        assertThat(route.toString()).isEqualTo("AER->KZN->LED (2,566.02 km)");
    }

    private Airport toAirport(String iataCode, String coordinates) {
        return Airport.builder().coordinates(coordinates).iataCode(iataCode).build();
    }
}