package org.example;

public interface Listener<T> {
    void notifyChanges(T event);
}
