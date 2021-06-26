package xyz.liamsho.mdauth.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.PlayerCache;

import java.util.UUID;
import java.util.logging.Level;

public class ManagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length <= 0 || args.length >= 3) {
            return false;
        }
        return switch (args[0]) {
            case "remove" -> removeCommand(commandSender, command, s, args);
            default -> false;
        };
    }

    public static boolean removeCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return false;
        }

        if (args[1].equals("!@#$|ALL_PLAYERS|%^&*")) {
            PlayerCache.RemoveCache();
            MDAuth.MDAuthLogger.info("已移除所有缓存");
            commandSender.sendMessage("已移除所有缓存");
            return true;
        }

        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
            PlayerCache.RemoveCache(uuid);
            MDAuth.MDAuthLogger.info("已从缓存删除玩家(使用 UUID) " + args[1]);
            commandSender.sendMessage("已从缓存删除玩家(使用 UUID)  " + ChatColor.GOLD + args[1]);
            return true;
        } catch (IllegalArgumentException ignored) { }
        var player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            MDAuth.MDAuthLogger.log(Level.WARNING, "从缓存删除玩家 " + args[1] + " 失败");
            return true;
        }
        PlayerCache.RemoveCache(player.getUniqueId());
        MDAuth.MDAuthLogger.info("已从缓存删除玩家 " + args[1]);
        commandSender.sendMessage("已从缓存删除玩家 " + ChatColor.GOLD + args[1]);
        return true;
    }
}
