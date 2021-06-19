package xyz.liamsho.minecraft.mdauth.commands.tabhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerTabHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return null;
        }

        if (args.length >= 3) {
            return null;
        }

        if (args.length == 1) {
            return Collections.singletonList("remove");
        }

        if (args.length == 2) {
            var list = new ArrayList<String>();
            list.add("<PlayerName / UUID>");
            list.add("!@#$|ALL_PLAYERS|%^&*");
            return list;
        }

        return null;
    }
}
