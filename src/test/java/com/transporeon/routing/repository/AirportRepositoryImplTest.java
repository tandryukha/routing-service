package com.transporeon.routing.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AirportRepositoryImplTest {

    private AirportRepositoryImpl airportRepository;

    @BeforeEach
    void setUp() {
        airportRepository = new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv"));
    }

    @Test
    void shouldLoadAllBeansFromCSV() {
        assertThat(airportRepository.findAll().size()).isEqualTo(57421);
    }

    @Test
    void shouldReadAllFieldsFromCSV() {
        assertThat(airportRepository.findAll())
                .allSatisfy(actual -> assertThat(actual).hasNoNullFieldsOrProperties());
    }

    @Test
    void shouldFailDuringCreationIfFileIsNotFound() {
        assertThrows(IllegalStateException.class, () -> new AirportRepositoryImpl(Path.of("non/existent/path/airports.csv")));
    }
}