package xyz.liamsho.mdauth.eventlisteners;


import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import xyz.liamsho.mdauth.RestrictedPlayers;

public class StopRestrictedPlayers implements Listener {

    @EventHandler
    public void stopMove(PlayerMoveEvent e) {
        cancelIfRestricted(e);
    }

    @EventHandler
    public void stopInteract(PlayerInteractEvent e) {
        cancelIfRestricted(e);
    }

    @EventHandler
    public void stopInteractAtEntity(PlayerInteractAtEntityEvent e) {
        cancelIfRestricted(e);
    }

    @EventHandler
    public void stopPortal(PlayerTeleportEvent e) {
        cancelIfRestricted(e);
    }

    @EventHandler
    public void stopInventory(InventoryOpenEvent e) {
        cancelIfRestricted(e);
    }

    private static void cancelIfRestricted(Cancellable e) {
        if (e instanceof PlayerEvent){
            if (RestrictedPlayers.Check(((PlayerEvent) e).getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        } else if (e instanceof InventoryEvent){
            if(RestrictedPlayers.Check(((InventoryOpenEvent) e).getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}
