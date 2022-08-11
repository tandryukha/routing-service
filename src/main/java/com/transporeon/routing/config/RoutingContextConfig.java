package com.transporeon.routing.config;

import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.repository.AirportRepositoryImpl;
import com.transporeon.routing.repository.FlightRepository;
import com.transporeon.routing.repository.FlightRepositoryImpl;
import com.transporeon.routing.service.PathFinder;
import com.transporeon.routing.service.SmartPathFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class RoutingContextConfig {

    @Bean
    public PathFinder pathFinder(RoutingConfig config) {
        return new SmartPathFinder(config.getGroundTransferThreshold(), config.getMaxStops());
    }

    @Bean
    public AirportRepository airportRepository() {
        return new AirportRepositoryImpl(Path.of("src/main/resources/airports.csv"));
    }

    @Bean
    public FlightRepository flightRepository() {
        return new FlightRepositoryImpl(Path.of("src/main/resources/flights.csv"));
    }

}
