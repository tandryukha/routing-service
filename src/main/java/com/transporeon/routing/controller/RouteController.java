package com.transporeon.routing.controller;

import com.transporeon.routing.service.RoutingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/route")
@RequiredArgsConstructor
public class RouteController {
    private final RoutingService routingService;

    @ApiOperation("Find the shortest route between two airports")
    @GetMapping
    public String findRoute(@ApiParam("Source airport IATA/ICAO code") String airport1,
                            @ApiParam("Destination airport IATA/ICAO code") String airport2) {
        return routingService.findRoute(airport1, airport2).map(Object::toString).orElse("");
    }

}
