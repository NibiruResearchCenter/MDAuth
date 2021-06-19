package xyz.liamsho.minecraft.mdauth.commands.tabhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CommonTabHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return null;
        }

        if (args.length >= 3) {
            return null;
        }

        if (args.length == 1) {
            return Collections.singletonList("bind");
        }

        if (args.length == 2) {
            return Collections.singletonList("<开黑啦 UID>");
        }

        return null;
    }
}
