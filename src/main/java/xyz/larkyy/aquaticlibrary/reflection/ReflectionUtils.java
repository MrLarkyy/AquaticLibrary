package xyz.larkyy.aquaticlibrary.reflection;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static boolean tryFieldSet(Field field, Object instance, Object value) {
        if (!field.trySetAccessible()) {
            return false;
        }
        try {
            field.set(instance,value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static boolean tryFieldSet(Class<?> clazz, String fieldName, Object instance, Object value) {
        try {
            return tryFieldSet(clazz.getDeclaredField(fieldName),instance,value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Bukkit.broadcastMessage("No such field");
            return false;
        }
    }
}
