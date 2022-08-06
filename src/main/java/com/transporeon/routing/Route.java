package com.transporeon.routing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Route {
    @Override
    public String toString() {
        Node<String> current = start;
        StringBuilder sb = new StringBuilder();
        String prefix = "";
        do {
            sb.append(prefix).append(current.getValue());
            prefix = "->";
            current = current.getNext();
        } while (current!=null);
        return sb.toString();
    }

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

        public boolean hasNext() {
            return next != null;
        }
    }
}
