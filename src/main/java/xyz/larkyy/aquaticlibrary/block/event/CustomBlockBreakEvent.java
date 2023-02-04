package xyz.larkyy.aquaticlibrary.block.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import xyz.larkyy.aquaticlibrary.block.CustomBlock;

public class CustomBlockBreakEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final CustomBlock customBlock;
    private final BlockBreakEvent event;

    public CustomBlockBreakEvent(CustomBlock customBlock, BlockBreakEvent event) {
        this.customBlock = customBlock;
        this.event = event;
    }
    public Player getPlayer() {
        return event.getPlayer();
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    public boolean isCancelled() {
        return event.isCancelled();
    }

    public void setCancelled(boolean bool) {
        event.setCancelled(bool);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
