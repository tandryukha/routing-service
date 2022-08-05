package com.transporeon.routing.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import com.transporeon.routing.entity.Flight;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FlightRepositoryImpl implements FlightRepository {

    private final List<Flight> flights;

    public FlightRepositoryImpl(Path pathToCsvFile) {
        List<Flight> result;
        try {
            result = loadFlights(pathToCsvFile);
        } catch (IOException e) {
            throw new IllegalStateException("The file flights.csv is not found");
        }
        flights = result;
    }

    @Override
    public List<Flight> findAll() {
        return flights;
    }

    private List<Flight> loadFlights(Path pathToCsvFile) throws IOException {
        try (Reader reader = Files.newBufferedReader(pathToCsvFile)) {
            return new CsvToBeanBuilder<Flight>(reader)
                    .withType(Flight.class)
                    .build()
                    .parse();
        }
    }
}
