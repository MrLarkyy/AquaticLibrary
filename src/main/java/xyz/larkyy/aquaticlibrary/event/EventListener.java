package xyz.larkyy.aquaticlibrary.event;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import xyz.larkyy.aquaticlibrary.AquaticLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EventListener<T extends Event> implements EventExecutor, Listener {

    private final List<EventAction<T>> actions;
    private final Class<T> eventClass;
    private final EventPriority priority;
    private final boolean ignoreCancelled;

    public EventListener(Class<T> eventClass, EventPriority priority, boolean ignoreCancelled){
        this.actions = new ArrayList<>();
        this.eventClass = eventClass;
        this.priority = priority;
        this.ignoreCancelled = ignoreCancelled;
    }

    public EventAction<T> addAction(Consumer<T> consumer) {
        var action = new EventAction<>(consumer,this);
        actions.add(action);
        return action;
    }

    public void removeAction(EventAction<T> action) {
        actions.remove(action);
    }

    public void register() {
        var plugin = AquaticLibrary.getPlugin();

        plugin.getServer().getPluginManager().registerEvent(eventClass, this, priority,this,plugin,ignoreCancelled);
        EventRegistry.get().registerListener(this);
    }

    public Class<T> getEventClass() {
        return eventClass;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (getEventClass().isInstance(event)) {
            int size = actions.size();
            for (int i = 0; i < actions.size(); i++) {
                var action = actions.get(i);
                if (action != null) {
                    action.accept((T) event);
                }
                if (size != actions.size()) {
                    size = actions.size();
                    i--;
                }
            }
        }
    }
}
