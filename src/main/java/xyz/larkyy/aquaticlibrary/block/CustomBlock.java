package xyz.larkyy.aquaticlibrary.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.larkyy.aquaticlibrary.block.event.CustomBlockInteractEvent;
import xyz.larkyy.aquaticlibrary.event.EventAction;
import xyz.larkyy.aquaticlibrary.event.EventRegistry;
import xyz.larkyy.aquaticlibrary.prop.Prop;
import xyz.larkyy.aquaticlibrary.prop.PropData;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomBlock extends Prop {
    private final List<EventAction<?>> eventActions = new ArrayList<>();

    public CustomBlock(Location location, PropData data) {
        super(location, data);
    }

    public void registerEvents() {
        unregisterEvents();
        var registry = ServiceManager.getService(EventRegistry.class);
        eventActions.add(registry.register(PlayerInteractEvent.class, e -> {
            var block = e.getClickedBlock();
            if (block == null) {
                return;
            }
            if (block.equals(getBlock())) {
                var event = new CustomBlockInteractEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
            }
        }));
        eventActions.add(registry.register(BlockBreakEvent.class, e -> {
            var block = e.getBlock();
            if (block.equals(getBlock())) {
                /*
                    TODO: Send a custom event
                 */
            }
        }));
        eventActions.add(registry.register(BlockPistonExtendEvent.class, e -> {
            if (e.getBlocks().contains(getBlock())) {
                /*
                    TODO: Send a custom event
                 */
            }
        }));
        eventActions.add(registry.register(BlockPistonRetractEvent.class, e -> {
            if (e.getBlocks().contains(getBlock())) {
                /*
                    TODO: Send a custom event
                 */
            }
        }));
        eventActions.add(registry.register(BlockExplodeEvent.class, e -> {
            if (e.getBlock().equals(getBlock())) {
                /*
                    TODO: Send a custom event
                 */
            }
        }));
    }

    public void unregisterEvents() {
        eventActions.forEach(EventAction::unregister);
        eventActions.clear();
    }

    @Override
    public void move(Location location) {
        despawn();
        setLocation(location);
        spawn();
    }

    @Override
    public void unload() {
        super.unload();
    }
    @Override
    public void load() {
        super.load();
    }

    public Block getBlock() {
        return getLocation().getBlock();
    }
}
