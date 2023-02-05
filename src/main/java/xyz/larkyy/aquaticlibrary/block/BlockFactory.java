package xyz.larkyy.aquaticlibrary.block;

import org.bukkit.Location;
import xyz.larkyy.aquaticlibrary.prop.PropData;

public interface BlockFactory {

    CustomBlock create(Location location, PropData propData);

}
