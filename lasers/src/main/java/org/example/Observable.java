package org.example;

public interface Observable<T> {
    void addListener(Listener<T> listener);
}
