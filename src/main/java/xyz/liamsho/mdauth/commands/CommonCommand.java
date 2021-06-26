package xyz.liamsho.mdauth.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.PlayerCache;
import xyz.liamsho.mdauth.RestrictedPlayers;
import xyz.liamsho.mdauth.httpapi.ApiRequest;
import xyz.liamsho.mdauth.httpapi.models.AddPlayerRequestModel;

import java.util.Objects;
import java.util.logging.Level;

public class CommonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length <= 0 || args.length >= 3) {
            return false;
        }
        return switch (args[0]) {
            case "bind" -> bindingCommand(commandSender, command, s, args);
            default -> false;
        };
    }

    public static boolean bindingCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length != 2){
            return false;
        }

        var player = Bukkit.getPlayer(commandSender.getName());
        if(player == null){
            MDAuth.MDAuthLogger.log(Level.SEVERE, "Inner error: can not find the command sender.");
            return true;
        }

        var reqModel = new AddPlayerRequestModel();
        reqModel.setKaiheilaUid(args[1]);
        reqModel.setPlayerName(player.getName());
        reqModel.setUUID(player.getUniqueId());
        var response = ApiRequest.AddPlayerRequest(reqModel);

        switch (Objects.requireNonNull(response).getCode()){
            case 0:
                break;
            case -300:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Add minecraft player request with empty body");
                player.sendMessage(ChatColor.RED + "出现内部错误, 请联系管理员, Code = -300");
                return true;
            case -400:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Add minecraft player request with invalid body");
                player.sendMessage(ChatColor.RED + "出现内部错误, 请联系管理员, Code = -400");
                return true;
            case -500:
                MDAuth.MDAuthLogger.info("Kaiheila UID " + args[1] + " of " + player.getName() + " is not correct or bilibili binding is null.");
                player.sendMessage(ChatColor.RED + "绑定失败: 开黑啦 UID 不正确, 或未绑定 Bilibili");
                return true;
            case -600:
                MDAuth.MDAuthLogger.info("Player " + player.getName() + " has already bind to minecraft");
                player.sendMessage(ChatColor.RED + "绑定失败: 您已经绑定过了");
                return true;
            default:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Unknown response code: " + response.getCode());
                player.sendMessage(ChatColor.RED + "出现内部错误, 请联系管理员, Code = Unknown");
                return true;
        }

        MDAuth.MDAuthLogger.info(player.getName() + " binding successfully with " +
                response.getData().getKaiheilaUsername() + "#" +
                response.getData().getKaiheilaUserIdentifyNumber() + ".");
        player.sendMessage(ChatColor.GREEN + "绑定成功! 开黑啦用户：" +
                response.getData().getKaiheilaUsername() + "#" +
                response.getData().getKaiheilaUserIdentifyNumber());

        var elementOnly = MDAuth.MDAuthConfiguration.getInt("elementOnly");
        if (elementOnly != response.getData().getElement() && response.getData().getElement() != 1) {
            switch (elementOnly) {
                case 1: player.kickPlayer("本服务器为"+ ChatColor.GOLD + ChatColor.BOLD + ChatColor.ITALIC + " 金弹幕兽 " + ChatColor.RESET + "限定"); break;
                case 2: player.kickPlayer("本服务器为"+ ChatColor.GREEN + ChatColor.BOLD + ChatColor.ITALIC + " 草弹幕兽 " + ChatColor.RESET + "限定"); break;
                case 3: player.kickPlayer("本服务器为"+ ChatColor.AQUA + ChatColor.BOLD + ChatColor.ITALIC + " 海弹幕兽 " + ChatColor.RESET + "限定"); break;
                case 4: player.kickPlayer("本服务器为"+ ChatColor.RED + ChatColor.BOLD + ChatColor.ITALIC + " 火弹幕兽 " + ChatColor.RESET + "限定"); break;
                case 5: player.kickPlayer("本服务器为"+ ChatColor.YELLOW + ChatColor.BOLD + ChatColor.ITALIC + " 土弹幕兽 " + ChatColor.RESET + "限定"); break;
                default: break;
            }
        }

        RestrictedPlayers.Remove(player.getUniqueId());
        PlayerCache.AddOrUpdateCache(response.getData());
        return true;
    }
}
