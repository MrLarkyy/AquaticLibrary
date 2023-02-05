package xyz.larkyy.aquaticlibrary.prop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropManager2 {

    private final Map<Class<? extends Prop>,PropContext2<? extends Prop>> contexts = new HashMap<>();

    public void loadProps() {
        contexts.forEach((c,context) -> context.loadProps());
    }

    public void unloadProps() {
        getProps().forEach(Prop::unload);
        contexts.values().forEach(c -> c.getProps().clear());
    }

    public <T extends Prop> void registerProp(T prop) {
        if (!contexts.containsKey(prop.getClass())) {
            return;
        }
        var context = contexts.get(prop.getClass());
        context.addProp(prop);
    }

    public <T extends Prop> void unregisterProp(T prop) {
        if (!contexts.containsKey(prop.getClass())) {
            return;
        }
        var context = contexts.get(prop.getClass());
        context.deleteProp(prop);
    }

    public <T extends Prop> void addProp(T prop) {
        if (!contexts.containsKey(prop.getClass())) {
            return;
        }
        PropContext2<T> context = (PropContext2<T>) getContext(prop.getClass());
        var props = context.getProps();
        if (!props.contains(prop)) {
            props.add(prop);
        }
    }

    public <T extends Prop> void removeProp(T prop) {
        if (!contexts.containsKey(prop.getClass())) {
            return;
        }
        var context = contexts.get(prop.getClass());
        context.getProps().remove(prop);
    }

    public List<Prop> getProps() {
        List<Prop> props = new ArrayList<>();
        contexts.values().forEach(c -> {
            props.addAll(c.getProps());
        });
        return props;
    }

    public <T extends Prop> PropContext2<T> getContext(Class<T> clazz) {
        return (PropContext2<T>) contexts.get(clazz);
    }

    public <T extends Prop> void addContext(Class<T> clazz, PropContext2<T> context) {
        contexts.put(clazz,context);
    }
}
