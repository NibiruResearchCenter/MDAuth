package xyz.liamsho.mdauth.sessions;

import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.RestrictedPlayers;
import xyz.liamsho.mdauth.common.CheckLoginMode;
import xyz.liamsho.mdauth.httpapi.ApiRequest;
import xyz.liamsho.mdauth.httpapi.CheckPlayerName;

import java.util.Objects;
import java.util.logging.Level;

import static xyz.liamsho.mdauth.MDAuth.LuckPermsApi;

public class AuthenticationSession {

    private final Player player;

    public boolean CrackedLoginJob;
    public boolean CrackedRegisterJob;
    public boolean AccountBindingAuthenticationJob;

    public AuthenticationSession(Player player) {
        this.CrackedLoginJob = false;
        this.CrackedRegisterJob = false;
        this.AccountBindingAuthenticationJob = false;
        this.player = player;
        RestrictedPlayers.Add(player.getUniqueId());
        player.setGameMode(GameMode.SURVIVAL);
    }

    public void CheckOfflineModeLogin() {
        // 确认是否是盗版登录
        if (CheckLoginMode.isOffline(player.getUniqueId(), player.getName())) {
            var useChinese = player.getLocale().contains("zh");
            if (Objects.requireNonNull(ApiRequest.GetCrackedPlayer(player.getUniqueId(), "")).getCode() == 0) {
                // 盗版已注册
                this.CrackedRegisterJob = true;
                MDAuth.MDAuthLogger.info("Player " + player.getName() + "(" + player.getUniqueId() + ") login in offline mode. Require password login.");
                player.sendMessage("[MDAuth]" + ChatColor.RED + "请使用指令 " + ChatColor.GOLD + "/account login <密码> " + ChatColor.RED + "登录");
                player.sendMessage("[MDAuth]" + ChatColor.RED + "Please use command " + ChatColor.GOLD + "/account login <Password> " + ChatColor.RED + "to login");
                return;
            }
            // 盗版未注册，所有 Job 均为 False
            // 检查未注册用户的 ID 是否和正版 ID 重复
            MDAuth.MDAuthLogger.info("Player " + player.getName() + "(" + player.getUniqueId() + ") login in offline mode. Unregistered.");
            var uuid = player.getUniqueId();
            var name = player.getName();
            MDAuth.MDAuthLogger.log(Level.WARNING, "Offline player name " + name + "(" + uuid + ") has been used by a online mode player.");
            if (CheckLoginMode.isOffline(uuid, name)) {
                if (CheckPlayerName.ExistInOnlinePlayerId(name)) {
                    player.kickPlayer("Your offline mode id has been used by a online mode player. " +
                            "您的离线模式登录 ID 已被正版玩家使用.");
                }
            }
            player.sendMessage("[MDAuth]" + ChatColor.RED + "您尚未注册, 请使用指令 " + ChatColor.GOLD + "/account reg <密码> " + ChatColor.RED + "注册");
            player.sendMessage("[MDAuth]" + ChatColor.RED + "You have not registered yet, please use command " +
                    ChatColor.GOLD + "/account reg <Password> " + ChatColor.RED + "to register");
        } else {
            // 正版登录，盗版登录Job 为 True
            this.CrackedRegisterJob = true;
            this.CrackedLoginJob = true;
            MDAuth.MDAuthLogger.info("Player " + player.getName() + "(" + player.getUniqueId() + ") login in online mode.");
        }
    }

    public void CheckAccountBinding() {
        var response = ApiRequest.GetPlayerRequest(player.getUniqueId(), player.getName());
        switch (response.getCode()) {
            case 0 -> {
                AccountBindingAuthenticationJob = true;
                if (player.hasPermission("group.energy")) {
                    if (!response.getData().getGuard()) {
                        LuckPermsApi.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().remove(Node.builder("group.energy").build()));
                        MDAuth.MDAuthLogger.info("Revoke group.energy permission node from " + player.getName() + "(" + player.getUniqueId() + ")");
                    }
                }
                else {
                    if (response.getData().getGuard()) {
                        LuckPermsApi.getUserManager().modifyUser(player.getUniqueId(), user -> user.data().add(Node.builder("group.energy").build()));
                        MDAuth.MDAuthLogger.info("Grant group.energy permission node for " + player.getName() + "(" + player.getUniqueId() + ")");
                    }
                }
            }
            case -100 -> {
                MDAuth.MDAuthLogger.log(Level.WARNING, "Inner error: Get minecraft player request do not have uuid parameter");
                player.sendMessage("[MDAuth] " + ChatColor.RED + "出现内部错误, 请联系管理员, Code = -100");
                player.sendMessage("[MDAuth] " + ChatColor.RED + "Inner exception, please contact the admin. Code = -100");
            }
            case -200 -> {
                MDAuth.MDAuthLogger.info("Can not find player " + player.getName() + " with uuid " + player.getUniqueId());
                player.sendMessage("[MDAuth]" + ChatColor.RED + "找不到已绑定的开黑啦或 Discord账号, 请先进行绑定, 详情请参考开黑啦或 Discord 中的 Minecraft 服务器公告频道");
                player.sendMessage("[MDAuth]" + ChatColor.RED + "Can't find kaiheila or discord account bound to this minecraft player, please bind first. " +
                        "For more details, please refer to the Minecraft server announcement channel in Discord or Kaiheila");
            }
        }
    }

    public void FinishSession() {
        RestrictedPlayers.Remove(player.getUniqueId());
    }
}
