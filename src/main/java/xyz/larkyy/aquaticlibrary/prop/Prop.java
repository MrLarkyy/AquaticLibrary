package xyz.larkyy.aquaticlibrary.prop;

import org.bukkit.Location;
import xyz.larkyy.aquaticlibrary.database.annotations.Table;

@Table(name = "prop")
public abstract class Prop {

    private Location location;
    private final PropData propData;

    public Prop(Location location, PropData propData) {
        this.location = location;
        this.propData = propData;
    }

    public void spawn() {
        onSpawn();
    }

    public void despawn() {
        onDespawn();
    }

    public abstract void onSpawn();

    public abstract void onDespawn();

    public abstract void onLoad();

    public  void load() {
        onLoad();
    }

    public void unload() {
        onUnload();
    }
    public abstract void onUnload();

    public Location getLocation() {
        return location;
    }

    public abstract void move(Location location);

    public PropData getPropData() {
        return propData;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
