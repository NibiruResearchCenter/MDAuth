package xyz.liamsho.mdauth.eventlisteners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.sessions.SessionManager;

import java.util.Objects;

public class JoinExitEvent implements Listener {
    @EventHandler
    public void JoinEvent (PlayerJoinEvent e) {
        var player = e.getPlayer();

        // 如果是 SA，Admin 权限组，则直接跳过认证阶段
        if (player.hasPermission("group.superAdmin") || player.hasPermission("group.admin")) {
            MDAuth.MDAuthLogger.info("Player " + player.getName() + " is sa or admin, skip auth.");
            return;
        }

        SessionManager.NewSession(Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())));
    }

    @EventHandler
    public void ExitEvent (PlayerQuitEvent e) {
        SessionManager.PlayerExit(e.getPlayer().getUniqueId());
    }
}
