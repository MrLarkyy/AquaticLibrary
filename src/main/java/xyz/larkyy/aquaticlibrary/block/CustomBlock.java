package xyz.larkyy.aquaticlibrary.block;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.larkyy.aquaticlibrary.block.event.*;
import xyz.larkyy.aquaticlibrary.event.EventAction;
import xyz.larkyy.aquaticlibrary.event.EventRegistry;
import xyz.larkyy.aquaticlibrary.prop.Prop;
import xyz.larkyy.aquaticlibrary.prop.PropData;
import xyz.larkyy.aquaticlibrary.service.ServiceManager;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomBlock extends Prop {
    private final List<EventAction<?>> eventActions = new ArrayList<>();
    private boolean breakable;
    private boolean explodable;
    private boolean pushable;
    private boolean interactable;

    public CustomBlock(Location location, PropData data) {
        super(location, data);
        var service = ServiceManager.getGenericService(BlockManager.class,this.getClass());
        if (service != null) {
            service.addProp(this);
        }
        breakable = true;
        explodable = true;
        pushable = true;
        interactable = true;
    }

    public void delete() {
        var service = ServiceManager.getGenericService(BlockManager.class,this.getClass());
        if (service != null) {
            unload();
            service.removeProp(this);
            service.unregisterProp(this);
        }
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
                if (!isInteractable()) {
                    e.setCancelled(true);
                }
                var event = new CustomBlockInteractEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
            }
        }));
        eventActions.add(registry.register(BlockBreakEvent.class, e -> {
            var block = e.getBlock();
            if (block.equals(getBlock())) {
                if (!isBreakable()) {
                    e.setCancelled(true);
                }
                var event = new CustomBlockBreakEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
            }
        }));
        eventActions.add(registry.register(BlockPistonExtendEvent.class, e -> {
            if (e.getBlocks().contains(getBlock())) {
                if (!isPushable()) {
                    e.setCancelled(true);
                }
                var event = new CustomBlockPistonExtendEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
            }
        }));
        eventActions.add(registry.register(BlockPistonRetractEvent.class, e -> {
            if (e.getBlocks().contains(getBlock())) {
                if (!isPushable()) {
                    e.setCancelled(true);
                }
                var event = new CustomBlockPistonRetractEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
            }
        }));
        eventActions.add(registry.register(BlockExplodeEvent.class, e -> {
            if (e.getBlock().equals(getBlock())) {
                if (!isExplodable()) {
                    e.setCancelled(true);
                }
                var event = new CustomBlockExplodeEvent(this,e);
                Bukkit.getPluginManager().callEvent(event);
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

    public boolean isBreakable() {
        return breakable;
    }

    public boolean isExplodable() {
        return explodable;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public void setExplodable(boolean explodable) {
        this.explodable = explodable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public Block getBlock() {
        return getLocation().getBlock();
    }
}
