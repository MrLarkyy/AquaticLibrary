package xyz.larkyy.aquaticlibrary.event;

import org.bukkit.event.Event;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventRegistry {

    private final Map<Class<? extends Event>,EventListener> listeners = new HashMap<>();

    public <T extends Event> EventAction<T> register(Class<T> clazz, Consumer<T> consumer) {
        EventListener<T> aquaticListener = listeners.get(clazz);
        if (aquaticListener != null) {
            return addActionToRegistered(aquaticListener,consumer);
        }
        else {
            aquaticListener = new EventListener<T>(clazz);
            aquaticListener.register();
            return aquaticListener.addAction(consumer);
        }
    }

    public void registerListener(EventListener<? extends Event> listener) {
        listeners.put(listener.getEventClass(),listener);
    }

    private <T extends Event> EventAction<T> addActionToRegistered(EventListener<T> listener, Consumer<T> consumer) {
        return listener.addAction(consumer);
    }

    public static EventRegistry get() {
        return ServiceManager.getService(EventRegistry.class);
    }
}
