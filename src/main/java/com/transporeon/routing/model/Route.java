package com.transporeon.routing.model;

import com.transporeon.routing.entity.Locatable;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@EqualsAndHashCode
public class Route<M extends Locatable<M>> {

    private final LinkedList<M> nodes = new LinkedList<>();
    private double distance = 0d;

    public Route(M startNode) {
        nodes.add(startNode);
    }

    public Route<M> add(M node) {
        distance += node.distanceTo(nodes.getLast());
        nodes.addLast(node);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route<?> route = (Route<?>) o;
        return nodes.equals(route.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }

    @Override
    public String toString() {
        String route = nodes.stream().map(Object::toString).collect(joining("->"));
        return route + format(" (%,.2f km)", getDistance());
    }

    public double getDistance() {
        return distance;
    }

}
