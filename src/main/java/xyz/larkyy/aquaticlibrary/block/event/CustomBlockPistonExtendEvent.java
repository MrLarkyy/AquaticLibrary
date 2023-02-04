package xyz.larkyy.aquaticlibrary.block.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPistonExtendEvent;
import xyz.larkyy.aquaticlibrary.block.CustomBlock;

public class CustomBlockPistonExtendEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final CustomBlock customBlock;
    private final BlockPistonExtendEvent event;

    public CustomBlockPistonExtendEvent(CustomBlock customBlock, BlockPistonExtendEvent event) {
        this.customBlock = customBlock;
        this.event = event;
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
