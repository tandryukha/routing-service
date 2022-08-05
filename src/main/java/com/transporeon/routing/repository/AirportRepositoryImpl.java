package com.transporeon.routing.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import com.transporeon.routing.entity.Airport;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AirportRepositoryImpl implements AirportRepository {

    private final List<Airport> airports;

    public AirportRepositoryImpl(Path pathToCsvFile) {
        List<Airport> result;
        try {
            result = loadAirports(pathToCsvFile);
        } catch (IOException e) {
            throw new IllegalStateException("The file airports.csv is not found");
        }
        airports = result;
    }

    @Override
    public List<Airport> findAll() {
        return airports;
    }

    private List<Airport> loadAirports(Path pathToCsvFile) throws IOException {
        try (Reader reader = Files.newBufferedReader(pathToCsvFile)) {
            return new CsvToBeanBuilder<Airport>(reader)
                    .withType(Airport.class)
                    .build()
                    .parse();
        }
    }
}
