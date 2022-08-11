package com.transporeon.routing.repository;

import com.transporeon.routing.entity.Airport;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class AirportRepositoryImpl extends CSVTemplateRepository<Airport> implements AirportRepository {

    private final Map<String, Airport> groupedAirports;

    public AirportRepositoryImpl(InputStream inputStream) {
        super(inputStream, Airport.class);
        groupedAirports = findAll().stream()
                .filter(this::isValid)
                .collect(toMap(Airport::getIataCode, Function.identity(), (airport, airport2) -> airport));
    }

    @Override
    public boolean isPresent(String iataCode) {
        return groupedAirports.containsKey(iataCode);
    }

    @Override
    public Map<String, Airport> findAllGrouped() {
        return groupedAirports;
    }

    private boolean isValid(Airport airport) {
        return isNotBlank(airport.getIataCode());
    }
}
