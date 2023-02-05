package xyz.larkyy.aquaticlibrary.prop;

import org.bukkit.Location;

public interface PropFactory {

    Prop create(Location location, PropData propData);

}
