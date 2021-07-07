package xyz.liamsho.mdauth.sessions;

import org.bukkit.entity.Player;
import xyz.liamsho.mdauth.MDAuth;
import xyz.liamsho.mdauth.RestrictedPlayers;

import java.util.*;

public class SessionManager {
    private static final Map<UUID, AuthenticationSession> sessions = new HashMap<>();

    public static void NewSession(Player player) {
        MDAuth.MDAuthLogger.info("New auth session for player " + player.getName() + "(" + player.getUniqueId() + ")");
        RestrictedPlayers.Add(player.getUniqueId());
        sessions.put(player.getUniqueId(), new AuthenticationSession(player));
        sessions.get(player.getUniqueId()).CheckOfflineModeLogin();
        sessions.get(player.getUniqueId()).CheckAccountBinding();
        LoginSessionJobCheck(player.getUniqueId());
    }

    // 完成绑定认证
    public static void AccountBindingAuthenticationSuccessfully(UUID uuid) {
        sessions.get(uuid).AccountBindingAuthenticationJob = true;
        LoginSessionJobCheck(uuid);
    }

    // 完成盗版注册
    public static void OfflineModeRegisterSuccessfully(UUID uuid) {
        sessions.get(uuid).CrackedRegisterJob = true;
        LoginSessionJobCheck(uuid);
    }

    // 完成盗版登陆
    public static void OfflineModeLoginSuccessfully(UUID uuid) {
        sessions.get(uuid).CrackedLoginJob = true;
        LoginSessionJobCheck(uuid);
    }

    // 确认登陆是否完成
    public static void LoginSessionJobCheck(UUID uuid) {
        if (CheckAccountBinding(uuid) && CheckFinishOfflineRegister(uuid) && CheckFinishOfflineLogin(uuid)) {
            MDAuth.MDAuthLogger.info("Player with uuid = {" + uuid.toString() + "} finished the auth session.");
            sessions.get(uuid).FinishSession();
            sessions.remove(uuid);
            RestrictedPlayers.Remove(uuid);
        }
    }

    // 确认是否完成了盗版注册
    public static boolean CheckFinishOfflineRegister(UUID uuid) {
        if (sessions.containsKey(uuid)) {
            return sessions.get(uuid).CrackedRegisterJob;
        }
        return true;
    }

    // 确认是否完成了盗版登陆
    public static boolean CheckFinishOfflineLogin(UUID uuid) {
        if (sessions.containsKey(uuid)) {
            return sessions.get(uuid).CrackedLoginJob;
        }
        return true;
    }

    // 确认是否完成了账号绑定
    public static boolean CheckAccountBinding(UUID uuid) {
        if (sessions.containsKey(uuid)) {
            return sessions.get(uuid).AccountBindingAuthenticationJob;
        }
        return true;
    }

    // 玩家退出，删除 Session
    public static void PlayerExit(UUID uuid) {
        if (sessions.containsKey(uuid)) {
            MDAuth.MDAuthLogger.info("Player with uuid = {" + uuid.toString() + "} left the game. Remove auth session.");
            sessions.get(uuid).FinishSession();
            sessions.remove(uuid);
        }
    }
}
