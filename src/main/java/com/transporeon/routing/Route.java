package com.transporeon.routing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Route {
    private final Node<String> start;
    private Node<String> end;

    public Route(String startValue) {
        this.start = new Node<>(startValue, null);
        this.end = start;
    }

    public Route add(String hopValue) {//todo refactor
        Node<String> newEnd = new Node<>(hopValue, null);
        end.setNext(newEnd);
        end = newEnd;
        return this;
    }

    @Data
    @AllArgsConstructor
    private static class Node<T> {
        private T value;
        private Node<T> next;
    }
}
