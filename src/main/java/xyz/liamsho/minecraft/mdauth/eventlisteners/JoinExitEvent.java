package xyz.liamsho.minecraft.mdauth.eventlisteners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.liamsho.minecraft.mdauth.MDAuth;
import xyz.liamsho.minecraft.mdauth.PlayerCache;
import xyz.liamsho.minecraft.mdauth.RestrictedPlayers;
import xyz.liamsho.minecraft.mdauth.httpapi.ApiRequest;

import java.util.logging.Level;

public class JoinExitEvent implements Listener {
    @EventHandler
    public void JoinEvent (PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }

        var uuid = e.getPlayer().getUniqueId();
        var name = e.getPlayer().getName();

        RestrictedPlayers.Add(uuid);
        e.getPlayer().setGameMode(GameMode.SPECTATOR);

        if (PlayerCache.Check(uuid)) {
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            RestrictedPlayers.Remove(uuid);
            return;
        }

        var response = ApiRequest.GetPlayerRequest(uuid, name);

        switch (response.getCode()) {
            case 0:
                break;
            case -100:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Get minecraft player request do not have uuid parameter");
                e.getPlayer().sendMessage(ChatColor.RED + "出现错误: ");
                return;
            case -200:
                MDAuth.MDAuthLogger.info("Can not find player " + name + " with uuid " + uuid);
                e.getPlayer().sendMessage(ChatColor.RED + "您暂未绑定开黑啦, 请先进行绑定");
                e.getPlayer().sendMessage("1. 在开黑啦 \"账号绑定频道\" 输入 \"/account query\" 进行查询，获取 Kaiheila UID");
                e.getPlayer().sendMessage("2. 在这里输入指令 \"/account bind <Kaiheila UID>\" 进行绑定");
                PlayerCache.RemoveCache(uuid);
                return;
        }

        e.getPlayer().setGameMode(GameMode.SURVIVAL);
        RestrictedPlayers.Remove(uuid);
        PlayerCache.AddOrUpdateCache(response.getData());
    }

    @EventHandler
    public void ExitEvent (PlayerQuitEvent e) {
        RestrictedPlayers.Remove(e.getPlayer().getUniqueId());
    }
}
