package xyz.larkyy.aquaticlibrary.service;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticlibrary.event.EventRegistry;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {

    static {
        services = new HashMap<>();
        genericServices = new HashMap<>();
        addService(new EventRegistry());
    }

    private ServiceManager() {
    }

    private static final Map<Class<?>, Object> services;
    private static final Map<Class<?>, Map<Class<?>,Object>> genericServices;

    public static <T> void addService(T instance) {
        services.put(instance.getClass(),instance);
    }
    public static <T> void addService(Class<T> clazz, T instance) {
        services.put(clazz,instance);
    }
    public static <T,G> void addGenericService(Class<G> genericClass, T instance) {
        var map = genericServices.computeIfAbsent(instance.getClass(), k -> new HashMap<>());
        map.put(genericClass,instance);
    }

    public static <T> T getService(Class<T> clazz) {
        return clazz.cast(services.get(clazz));
    }
    public static <T,G> T getGenericService(Class<T> clazz, Class<G> genericClass) {
        var map = genericServices.computeIfAbsent(clazz, k -> new HashMap<>());
        return clazz.cast(map.get(genericClass));
    }
}
