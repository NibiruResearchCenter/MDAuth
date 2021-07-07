package xyz.liamsho.mdauth.commands;

import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.common.CheckLoginMode;
import xyz.liamsho.mdauth.common.Encryption;
import xyz.liamsho.mdauth.httpapi.ApiRequest;
import xyz.liamsho.mdauth.httpapi.models.AddPlayerRequestModel;
import xyz.liamsho.mdauth.sessions.SessionManager;

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class CommonCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            return false;
        }

        if (args.length <= 0 || args.length >= 4) {
            return false;
        }

        return switch (args[0]) {
            case "bind" -> bindingCommand(commandSender, command, s, args);
            case "reg" -> regCommand(commandSender, command, s, args);
            case "login" -> loginCommand(commandSender, command, s, args);
            default -> false;
        };
    }

    // /account bind(0) [discord/kaiheila](1) [uid](2)
    public static boolean bindingCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 3){
            return false;
        }

        var platform = args[1];
        var uid = args[2];

        // 确认平台字符串
        if (!platform.equals("kaiheila")) {
            if (!platform.equals("discord")) {
                return false;
            }
        }

        var player = Bukkit.getPlayer(commandSender.getName());
        if(player == null){
            MDAuth.MDAuthLogger.log(Level.SEVERE, "Inner error: can not find the command sender.");
            return true;
        }

        var useChinese = player.getLocale().contains("zh");
        MDAuth.MDAuthLogger.info(player.getLocale());

        if (!SessionManager.CheckFinishOfflineLogin(player.getUniqueId())) {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "请先完成登录" : "Please finish login first."));
            return true;
        }

        if (SessionManager.CheckAccountBinding(player.getUniqueId())) {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "您已经完成了绑定" : "You have already finished account binging."));
            return true;
        }

        // 发送请求
        var reqModel = new AddPlayerRequestModel();
        reqModel.setUid(uid);
        reqModel.setPlatform(platform);
        reqModel.setPlayerName(player.getName());
        reqModel.setUuid(player.getUniqueId().toString());
        reqModel.setLegitCopy(CheckLoginMode.isOffline(player.getUniqueId(), player.getName()));
        var response = ApiRequest.AddPlayerRequest(reqModel);

        // 确认状态码
        switch (Objects.requireNonNull(response).getCode()){
            case 0:
                break;
            case -300:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Add minecraft player request with empty body");
                player.sendMessage("[MDAuth] " + ChatColor.RED +
                        (useChinese ? "出现内部错误, 请联系管理员, Code = -300" : "Inner exception, please contact the admin. Code = -300"));
                return true;
            case -400:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Add minecraft player request with invalid body");
                player.sendMessage("[MDAuth] " + ChatColor.RED +
                        (useChinese ? "出现内部错误, 请联系管理员, Code = -400" : "Inner exception, please contact the admin. Code = -400"));
                return true;
            case -500:
                if (platform.equals("kaiheila")) {
                    MDAuth.MDAuthLogger.info("UID " + args[1] + " of " + player.getName() + " is not correct, bilibili binding or element is null");
                    player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "绑定失败: 找不到对应的开黑啦 UID, 或未绑定 Bilibili, 或未获得弹幕兽属性角色" :
                            "Binding failed: Can't find the corresponding Kaiheila UID, or unbound Bilibili, or have not obtained the Element role."));
                } else {
                    MDAuth.MDAuthLogger.info("UID" + args[1] + " of " + player.getName() + " is not correct.");
                    player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "绑定失败: 找不到对应的 Discord UID" :
                            "Binding failed: Can't find the corresponding Discord UID."));
                }
                return true;
            case -600:
                MDAuth.MDAuthLogger.info("Player " + player.getName() + " has already bind to minecraft");
                player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "绑定失败: 您已经绑定过了" :
                        "Binding failed: You have already bind to minecraft."));
                return true;
            default:
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Unknown response code: " + response.getCode());
                player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "出现内部错误, 请联系管理员, Code = Unknown" :
                        "Inner exception, please contact the admin. Code = Unknown"));
                return true;
        }

        // 绑定成功
        MDAuth.MDAuthLogger.info(player.getName() + " binding successfully with " +
                response.getData().getUsername() + "#" +
                response.getData().getIdentifyNumber() + " from " +
                platform + ".");
        if (platform.equals("kaiheila")) {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "绑定成功! 开黑啦用户: " : "Binding successfully! Kaiheila user: ") +
                    response.getData().getUsername() + "#" +
                    response.getData().getIdentifyNumber());
        } else {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "绑定成功! Discord 用户: " : "Binding successfully! Discord user: ") +
                    response.getData().getUsername() + "#" +
                    response.getData().getIdentifyNumber());
        }

        // 修改权限组
        Node node;
        switch (response.getData().getElement()) {
            case 2 -> node = Node.builder("group.herba").build();
            case 3 -> node = Node.builder("group.aqua").build();
            case 4 -> node = Node.builder("group.flame").build();
            case 5 -> node = Node.builder("group.earth").build();
            default -> node = null;
        }
        if (node == null) {
            player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "权限组加载失败, 请联系管理员" :
                    "Permission group loading error, please contact the admin."));
            return true;
        }

        var lpUser = MDAuth.LuckPermsApi.getPlayerAdapter(Player.class).getUser(player);
        lpUser.data().add(node);
        MDAuth.MDAuthLogger.info("Grant " + node.getKey() + " permission node for " + player.getName() + "(" + player.getUniqueId() + ")");

        if (response.getData().getGuard()) {
            lpUser.data().add(Node.builder("group.gold").build());
            MDAuth.MDAuthLogger.info("Grant group.gold permission node for " + player.getName() + "(" + player.getUniqueId() + ")");
        }

        MDAuth.LuckPermsApi.getUserManager().saveUser(lpUser);

        // 操作完成
        SessionManager.AccountBindingAuthenticationSuccessfully(player.getUniqueId());
        return true;
    }

    // /account reg(0) [password](1)
    public static boolean regCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return false;
        }

        var player = Bukkit.getPlayer(commandSender.getName());
        if(player == null){
            MDAuth.MDAuthLogger.log(Level.SEVERE, "Inner error: can not find the command sender.");
            return true;
        }

        var useChinese = player.getLocale().contains("zh");

        if (SessionManager.CheckFinishOfflineRegister(player.getUniqueId())) {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "您已经注册过了" : "You have already registered."));
            return true;
        }

        var password = args[1];
        var pattern = "^(?![A-Za-z]+$)(?![A-Z0-9]+$)(?![a-z0-9]+$)(?![a-z\\W]+$)(?![A-Z\\W]+$)(?![0-9\\W]+$)[a-zA-Z0-9\\W]{8,16}$";
        var match = Pattern.matches(pattern, password);
        if (!match){
            player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "密码要求 8 - 16 位, 至少包含大写字母, 小写字母, 特殊符号, 数字中的任意三项" :
                    "Password requires 8 - 16 digits, containing at least three of uppercase letters, lowercase letters, special characters, and numbers."));
            return true;
        }
        var hashedPassword = Encryption.SHA512Hash(password);

        var operationResult = ApiRequest.AddCrackedPlayer(player.getUniqueId(), hashedPassword);
        switch (Objects.requireNonNull(operationResult).getCode()) {
            case 0:
                break;
            case -1500:
                player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "内部错误, 请联系管理员. Code = -1500" :
                        "Inner exception, please contact the administrator. Code = -1500"));
                return true;
            case -1600:
                player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "内部错误, 请联系管理员. Code = -1600" :
                        "Inner exception, please contact the administrator. Code = -1600"));
                return true;
            case -1700:
                player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "您已经注册过了" : "You have already registered."));
                return true;
            default:
                player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "未知错误. Code = " : "Unknown error. Code = ") + operationResult.getCode());
                return true;
        }

        player.sendMessage("[MDAuth] " + (useChinese ? ChatColor.GREEN + "注册成功，使用指令 " + ChatColor.GOLD + "/account login <密码>" + ChatColor.GREEN + " 进行登录" :
                ChatColor.GREEN + "Register successfully, use command " + ChatColor.GOLD + "/account login <Password>" + ChatColor.GREEN + " to login."));
        SessionManager.OfflineModeRegisterSuccessfully(player.getUniqueId());
        return true;
    }

    // /account login(0) [password](1)
    public static boolean loginCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 2) {
            return false;
        }

        var player = Bukkit.getPlayer(commandSender.getName());
        if(player == null){
            MDAuth.MDAuthLogger.log(Level.SEVERE, "Inner error: can not find the command sender.");
            return true;
        }

        var useChinese = player.getLocale().contains("zh");

        if (SessionManager.CheckFinishOfflineLogin(player.getUniqueId())) {
            player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "您已经完成登录了" :
                    "You are already logged in."));
            return true;
        }
        var password = args[1];

        var hashedPassword = Encryption.SHA512Hash(password);

        var operationResult = ApiRequest.GetCrackedPlayer(player.getUniqueId(), hashedPassword);
        switch (Objects.requireNonNull(operationResult).getCode()) {
            case -1200:
                player.sendMessage("[MDAuth] " + (useChinese ? ChatColor.RED + "您尚未注册, 请使用指令 " + ChatColor.GOLD + "/account reg <密码>" + ChatColor.RED + "进行注册" :
                        ChatColor.RED + "You are not registered, please use command " + ChatColor.GOLD + "/account reg <Password>" + ChatColor.RED + "to register."));
                return true;
            case 0:
                break;
            case -1100:
                player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "内部错误, 请联系管理员. Code = -1100" :
                        "Inner exception, please contact the administrator. Code = -1100"));
                return true;
            default:
                player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "未知错误. Code = " : "Unknown error. Code = ") + operationResult.getCode());
                return true;
        }

        if (!operationResult.getVerifyStatus()) {
            player.sendMessage("[MDAuth] " + ChatColor.RED + (useChinese ? "密码错误, 登录失败" :
                    "Invalid password, login failed."));
            return true;
        }
        player.sendMessage("[MDAuth] " + ChatColor.GREEN + (useChinese ? "登录成功" : "Login successfully."));
        SessionManager.OfflineModeLoginSuccessfully(player.getUniqueId());
        return true;
    }
}
