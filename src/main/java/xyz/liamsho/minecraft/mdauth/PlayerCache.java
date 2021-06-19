package xyz.liamsho.minecraft.mdauth;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.liamsho.minecraft.mdauth.httpapi.models.PlayerModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerCache {
    public static FileConfiguration data;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public static void Write() {
        try {
            File dataFile = new File(MDAuth.MDAuthPlugin.getDataFolder(), "cache.yml");
            data.save(dataFile);
        } catch (IOException e) {
            MDAuth.MDAuthLogger.log(Level.WARNING, "配置数据未能保存，可能产生回档问题！");
            e.printStackTrace();
        }
    }

    public static void Read() {
        File dataFile = new File(MDAuth.MDAuthPlugin.getDataFolder(), "cache.yml");
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public static void AddOrUpdateCache(PlayerModel player) {
        var uuid = player.getMinecraftUUID();
        data.set(uuid + ".kaiheila_username", player.getKaiheilaUsername());
        data.set(uuid + ".kaiheila_user_identify_number", player.getKaiheilaUserIdentifyNumber());
        data.set(uuid + ".bilibili_guard_level", player.getBilibiliGuardLevel());
        data.set(uuid + ".minecraft_player_name", player.getMinecraftPlayerName());
        data.set(uuid + ".element", player.getElement());
        data.set(uuid + ".last_update", sdf.format(Date.from(Instant.now())));
        Write();
    }

    public static void RemoveCache(UUID uuid) {
        data.set(uuid + ".kaiheila_username", null);
        data.set(uuid + ".kaiheila_user_identify_number", null);
        data.set(uuid + ".bilibili_guard_level", null);
        data.set(uuid + ".minecraft_player_name", null);
        data.set(uuid + ".element", null);
        data.set(uuid + ".last_update", null);
        Write();
    }

    public static void RemoveCache() {
        MDAuth.MDAuthPlugin.saveResource("cache.yml", true);
        Read();
    }

    public static boolean Check(UUID uuid) {
        if (data.contains(uuid.toString() + ".last_update")) {
            long lastUpdate = data.getLong(uuid + ".last_update");
            long now = Long.parseLong(sdf.format(Date.from(Instant.now())));
            return now - lastUpdate < 10000000;
        }
        return false;
    }
}
