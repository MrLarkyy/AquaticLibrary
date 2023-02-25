package xyz.larkyy.aquaticlibrary.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Nullable;
import xyz.larkyy.aquaticlibrary.prop.PropData;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

public class VanillaBlock extends CustomBlock{
    public VanillaBlock(Location location, PropData data) {
        super(location, data);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }

    public static @Nullable VanillaBlock create(Location location) {
        if (location == null) {
            return null;
        }
        return create(location.getBlock());
    }

    public static @Nullable VanillaBlock create(Block block) {
        if (block == null) {
            return null;
        }
        if (block.getType() == Material.AIR) {
            return null;
        }
        var manager = ServiceManager.getService(BlockManager2.class);
        var cb = manager.getCustomBlock(block);
        if (cb != null) {
            return null;
        }
        return new VanillaBlock(block.getLocation(),new PropData());
    }
}
