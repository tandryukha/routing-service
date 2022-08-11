package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Flight;

import java.io.InputStream;

public class FlightRepositoryImpl extends CSVTemplateRepository<Flight> implements FlightRepository {

    public FlightRepositoryImpl(InputStream inputStream) {
        super(inputStream, Flight.class);
    }

}
