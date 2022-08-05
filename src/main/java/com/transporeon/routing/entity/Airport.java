package com.transporeon.routing.entity;

import lombok.Data;

@Data
public class Airport {
    private String ident;
    private String type;
    private String name;
    private String elevationFt;
    private String continent;
    private String isoCountry;
    private String isoRegion;
    private String municipality;
    private String gpsCode;
    private String iataCode;
    private String localCode;
    private String coordinates;

}
