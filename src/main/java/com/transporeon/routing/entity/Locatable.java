package com.transporeon.routing.entity;

public interface Locatable<T> {
    double distanceTo(T other);
}
