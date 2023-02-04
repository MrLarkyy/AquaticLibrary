package xyz.larkyy.aquaticlibrary.prop;

import java.util.ArrayList;
import java.util.List;

public abstract class PropManager<T extends Prop> {

    private final PropContext<T> context;
    private List<T> props = new ArrayList<>();

    public PropManager(PropContext<T> propContext) {
        this.context = propContext;
    }

    public void loadProps() {
        props = context.loadProps();
    }

    public void unloadProps() {
        props.forEach(Prop::unload);
        props.clear();
    }

    public void registerProp(T prop) {
        context.addProp(prop);
    }

    public void unregisterProp(T prop) {
        context.deleteProp(prop);
    }

    public void addProp(T prop) {
        props.add(prop);
    }

    public void removeProp(T prop) {
        props.remove(prop);
    }

    public List<T> getProps() {
        return props;
    }

    public PropContext<T> getContext() {
        return context;
    }
}
