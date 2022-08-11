package com.transporeon.routing.entity;

public interface Location<T> {
    double distanceTo(T other);
}
