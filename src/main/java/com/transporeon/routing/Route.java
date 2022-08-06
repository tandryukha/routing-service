package com.transporeon.routing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Route<M> {
    @Override
    public String toString() {
        Node<String> current = start;
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        do {
            sb.append(prefix).append(current.getValue());
            prefix = "->";
            current = current.getNext();
        } while (current != null);
        return sb.toString();
    }

    private final Node<String> start;
    private Node<String> end;

    public Route(M startHop) {
        this.start = new Node<>(startHop.toString(), null);
        this.end = start;
    }

    public Route<M> add(M hopValue) {//todo refactor
        Node<String> newEnd = new Node<>(hopValue.toString(), null);
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
