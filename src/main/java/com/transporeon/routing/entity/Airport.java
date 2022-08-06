package com.transporeon.routing.entity;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @EqualsAndHashCode.Include
    @ToString.Include
    private String iataCode;
    private String localCode;
    /**
     * longitude,latitude csv
     */
    private String coordinates;

    /**
     * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is,
     * the shortest distance over the earth’s surface – giving an ‘as-the-crow-flies’ distance
     * between the points (ignoring any hills they fly over, of course!).
     *
     * Haversine
     * formula:	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
     * c = 2 ⋅ atan2( √a, √(1−a) )
     * d = R ⋅ c
     * where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
     * note that angles need to be in radians to pass to trig functions!
     *
     * Read more:
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">link1</a>,
     * <a href="https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/">link2</a>
     *
     * @param other Airport to calculate distance to
     * @return distance between two airports in km
     *
     *
     */
    public double distanceTo(Airport other) {
        String[] coords1 = this.coordinates.split(",");
        String[] coords2 = other.coordinates.split(",");
        double lon1 = Double.parseDouble(coords1[0]);
        double lat1 = Double.parseDouble(coords1[1]);
        double lon2 = Double.parseDouble(coords2[0]);
        double lat2 = Double.parseDouble(coords2[1]);

        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);
        double earthRadius = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return earthRadius * c;
    }
}
