package com.transporeon.routing.model;

import com.transporeon.routing.entity.Location;

import java.util.LinkedList;
import java.util.Objects;

import static com.transporeon.routing.model.Route.PathNode.Type.AIR;
import static com.transporeon.routing.model.Route.PathNode.Type.GROUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class Route<M extends Location<M>> {

    private final LinkedList<PathNode<M>> pathNodes = new LinkedList<>();
    private double distance = 0d;

    public Route(M startNode) {
        pathNodes.add(new PathNode<>(startNode, AIR));
    }

    public Route<M> add(M node) {
        return addPathNode(new PathNode<>(node, AIR));
    }

    public Route<M> addViaGround(M node) {
        return addPathNode(new PathNode<>(node, GROUND));
    }

    private Route<M> addPathNode(PathNode<M> pathNode) {
        distance += pathNode.node.distanceTo(pathNodes.getLast().node);
        pathNodes.addLast(pathNode);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route<?> route = (Route<?>) o;
        return pathNodes.equals(route.pathNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathNodes);
    }

    @Override
    public String toString() {
        String route = pathNodes.stream().map(this::nodeToString).collect(joining()).substring(2);
        return route + format(" (%,.2f km)", getDistance());
    }

    private String nodeToString(PathNode<M> pathNode) {
        String delimiter = pathNode.type == PathNode.Type.GROUND ? "=>" : "->";
        return delimiter + pathNode.node.toString();
    }

    public double getDistance() {
        return distance;
    }

    record PathNode<M>(M node, Type type) {
        enum Type {AIR, GROUND}
    }
}
