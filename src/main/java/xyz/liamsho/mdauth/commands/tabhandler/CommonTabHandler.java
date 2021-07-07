package xyz.liamsho.mdauth.commands.tabhandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import xyz.liamsho.mdauth.RestrictedPlayers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommonTabHandler implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return null;
        }

        if (!RestrictedPlayers.Check(Objects.requireNonNull(Bukkit.getPlayer(commandSender.getName())).getUniqueId())) {
            return null;
        }

        var useChinese = Objects.requireNonNull(Bukkit.getPlayer(commandSender.getName()))
                .getLocale().contains("zh");

        if (args.length >= 4) {
            return null;
        }

        if (args.length == 1) {
            var list = new ArrayList<String>();
            list.add("bind");
            list.add("reg");
            list.add("login");
            return list;
        }

        if (args.length == 2) {
            if (args[0].contains("b")) {
                var list = new ArrayList<String>();
                list.add("kaiheila");
                list.add("discord");
                return list;
            } else {
                if (useChinese) {
                    return Collections.singletonList("<密码>");
                }
                return Collections.singletonList("<Password>");
            }
        }

        if (args.length == 3) {
            return Collections.singletonList("<UID>");
        }

        return null;
    }
}
