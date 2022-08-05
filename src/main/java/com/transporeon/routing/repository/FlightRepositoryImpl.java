package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Flight;

import java.nio.file.Path;

public class FlightRepositoryImpl extends CSVTemplateRepository<Flight> implements FlightRepository {

    public FlightRepositoryImpl(Path pathToCsvFile) {
        super(pathToCsvFile, Flight.class);
    }

}
