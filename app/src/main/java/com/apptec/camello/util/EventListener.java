package com.apptec.camello.util;

/**
 * Is a listener of a event
 *
 * @param <T> Generic class
 */
public interface EventListener<T> {
    /**
     * Called when the #onEvent method from the {@link EventObserver} is called
     *
     * @param t It could be null
     */
    void onEvent(T t);
}