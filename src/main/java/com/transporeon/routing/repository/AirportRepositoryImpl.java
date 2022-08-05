package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Airport;

import java.nio.file.Path;

public class AirportRepositoryImpl extends CSVTemplateRepository<Airport> implements AirportRepository {

    public AirportRepositoryImpl(Path pathToCsvFile) {
        super(pathToCsvFile, Airport.class);
    }

}
