package com.transporeon.routing.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FlightRepositoryImplTest {

    private FlightRepositoryImpl airportRepository;

    @BeforeEach
    void setUp() {
        airportRepository = new FlightRepositoryImpl(Path.of("src/main/resources/flights.csv"));
    }

    @Test
    void shouldLoadAllFlightsFromCSV() {
        assertThat(airportRepository.findAll().size()).isEqualTo(67663);
    }

    @Test
    void shouldReadAllFieldsFromCSV() {
        assertThat(airportRepository.findAll())
                .allSatisfy(actual -> assertThat(actual).hasNoNullFieldsOrProperties());
    }

    @Test
    void shouldFailDuringCreationIfFileIsNotFound() {
        assertThrows(IllegalStateException.class, () -> new FlightRepositoryImpl(Path.of("non/existent/path/flights.csv")));
    }
}