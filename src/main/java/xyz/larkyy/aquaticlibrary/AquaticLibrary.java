package xyz.larkyy.aquaticlibrary;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

public class AquaticLibrary {

    private AquaticLibrary() {

    }

    public static void init(JavaPlugin plugin) {
        ServiceManager.addService(JavaPlugin.class,plugin);
    }

    public static JavaPlugin getPlugin() {
        return ServiceManager.getService(JavaPlugin.class);
    }
}
