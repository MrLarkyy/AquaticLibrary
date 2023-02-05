package xyz.larkyy.aquaticlibrary.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventRegistry {

    private final Map<Class<? extends Event>,EventListener> listeners = new HashMap<>();

    public <T extends Event> EventAction<T> register(Class<T> clazz, Consumer<T> consumer) {
        return register(clazz,consumer,EventPriority.NORMAL,false);
    }
    public <T extends Event> EventAction<T> register(Class<T> clazz, Consumer<T> consumer, EventPriority priority) {
        return register(clazz,consumer,priority,false);
    }
    public <T extends Event> EventAction<T> register(Class<T> clazz, Consumer<T> consumer, boolean ignoreCancelled) {
        return register(clazz,consumer,EventPriority.NORMAL,ignoreCancelled);
    }
    public <T extends Event> EventAction<T> register(Class<T> clazz, Consumer<T> consumer, EventPriority priority, boolean ignoreCancelled) {
        EventListener<T> aquaticListener = listeners.get(clazz);
        if (aquaticListener != null) {
            return addActionToRegistered(aquaticListener,consumer);
        }
        else {
            aquaticListener = new EventListener<>(clazz,priority,ignoreCancelled);
            aquaticListener.register();
            return aquaticListener.addAction(consumer);
        }
    }

    void registerListener(EventListener<? extends Event> listener) {
        listeners.put(listener.getEventClass(),listener);
    }

    private <T extends Event> EventAction<T> addActionToRegistered(EventListener<T> listener, Consumer<T> consumer) {
        return listener.addAction(consumer);
    }

    public static EventRegistry get() {
        return ServiceManager.getService(EventRegistry.class);
    }
}
