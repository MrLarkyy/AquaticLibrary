package xyz.larkyy.aquaticlibrary;

import org.bukkit.Material;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticlibrary.item.CustomItem;
import xyz.larkyy.aquaticlibrary.item.CustomItemHandler;
import xyz.larkyy.aquaticlibrary.prop.PropManager2;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

public class AquaticLibrary {

    private AquaticLibrary() {

    }

    public static void init(JavaPlugin plugin) {
        ServiceManager.addService(JavaPlugin.class,plugin);
        ServiceManager.addService(new CustomItemHandler());
        ServiceManager.addService(new PropManager2());

        var item = CustomItem.create(Material.STONE);
        item.addPersistentData(PersistentDataType.INTEGER,1);
        item.getItem();
    }

    public static JavaPlugin getPlugin() {
        return ServiceManager.getService(JavaPlugin.class);
    }
}
