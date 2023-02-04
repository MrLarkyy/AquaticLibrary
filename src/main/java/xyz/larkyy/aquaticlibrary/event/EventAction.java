package xyz.larkyy.aquaticlibrary.event;

import org.bukkit.event.Event;

import java.util.function.Consumer;

public class EventAction<T extends Event> {

    private final Consumer<T> consumer;
    private final EventListener<T> listener;

    public EventAction(Consumer<T> consumer, EventListener<T> listener) {
        this.consumer = consumer;
        this.listener = listener;
    }

    public void unregister() {
        listener.removeAction(this);
    }

    public void accept(T event) {
        consumer.accept(event);
    }

}
