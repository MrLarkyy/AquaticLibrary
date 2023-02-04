package xyz.larkyy.aquaticlibrary.block.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import xyz.larkyy.aquaticlibrary.block.CustomBlock;

public class CustomBlockInteractEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final CustomBlock customBlock;
    private final PlayerInteractEvent event;

    public CustomBlockInteractEvent(CustomBlock customBlock, PlayerInteractEvent event) {
        this.customBlock = customBlock;
        this.event = event;
    }

    public Action getAction() {
        return event.getAction();
    }

    public EquipmentSlot getHand() {
        return event.getHand();
    }

    public Player getPlayer() {
        return event.getPlayer();
    }

    public CustomBlock getCustomBlock() {
        return customBlock;
    }

    public boolean isCancelled() {
        return event.useInteractedBlock() == Result.DENY;
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
