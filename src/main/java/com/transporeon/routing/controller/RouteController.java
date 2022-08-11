package com.transporeon.routing.controller;

import com.transporeon.routing.exception.NoAirportException;
import com.transporeon.routing.repository.AirportRepository;
import com.transporeon.routing.service.RoutingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController("/route")
@RequiredArgsConstructor
public class RouteController {
    private final RoutingService routingService;
    private final AirportRepository airportRepository;

    @ApiOperation("Find the shortest route between two airports")
    @GetMapping
    public String findRoute(@ApiParam("Source airport IATA/ICAO code") @NotBlank String airport1,
                            @ApiParam("Destination airport IATA/ICAO code") @NotBlank String airport2) {
        validate(airport1);
        validate(airport2);
        return routingService.findRoute(airport1, airport2).map(Object::toString).orElse("");
    }

    private void validate(String airport) {
        if (!airportRepository.isPresent(airport)) {
            throw new NoAirportException(String.format("Airport %s not found", airport));
        }
    }

}
