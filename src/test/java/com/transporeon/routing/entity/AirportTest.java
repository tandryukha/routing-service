package com.transporeon.routing.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class AirportTest {

    /**
     * <a href="https://www.sunearthtools.com/tools/distance.php">Expected distance calculator</a>
     */
    @ParameterizedTest
    @CsvSource({
            "-74.93360137939453,40.07080078125,-87.65950012207031,40.45859909057617,1080.01",
            "24.832799911499997, 59.41329956049999,36.290000915527344, 49.924800872802734,1283.37",
            "0.1246,51.5007,74.0445,40.6892,5574.8"}
    )
    void shouldCalculateDistanceCorrectly(String lon1, String lat1, String lon2, String lat2, double expected) {
        Airport airport1 = Airport.builder().coordinates(String.format("%s, %s", lon1, lat1)).build();
        Airport airport2 = Airport.builder().coordinates(String.format("%s, %s", lon2, lat2)).build();
        assertThat(airport1.distanceTo(airport2)).isCloseTo(expected, within(0.5d));
    }

    @Test
    void shouldBeZeroDistanceToItself() {
        Airport airport = Airport.builder().coordinates("-74.93360137939453,40.07080078125").build();
        assertThat(airport.distanceTo(airport)).isEqualTo(0);
    }
}