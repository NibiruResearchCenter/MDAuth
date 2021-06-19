package xyz.liamsho.minecraft.mdauth;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.liamsho.minecraft.mdauth.commands.CommonCommand;
import xyz.liamsho.minecraft.mdauth.commands.ManagerCommand;
import xyz.liamsho.minecraft.mdauth.commands.tabhandler.CommonTabHandler;
import xyz.liamsho.minecraft.mdauth.commands.tabhandler.ManagerTabHandler;
import xyz.liamsho.minecraft.mdauth.eventlisteners.JoinExitEvent;
import xyz.liamsho.minecraft.mdauth.eventlisteners.StopRestrictedPlayers;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MDAuth extends JavaPlugin {

    public static Plugin MDAuthPlugin;
    public static Logger MDAuthLogger;
    public static FileConfiguration MDAuthConfiguration;

    @Override
    public void onLoad() {
        saveResource("cache.yml", false);
    }

    @Override
    public void onEnable() {
        MDAuthPlugin = this;

        MDAuthLogger = getLogger();
        MDAuthLogger.info("已启用 MDAuth");

        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            MDAuthLogger.log(Level.WARNING, "Configuration file does not exist. Plugin won't be functional.");
            this.setEnabled(false);
            return;
        }

        MDAuthConfiguration = getConfig();

        PlayerCache.Read();

        if (Bukkit.getPluginCommand("account") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("account")).setExecutor(new CommonCommand());
            Objects.requireNonNull(Bukkit.getPluginCommand("account")).setTabCompleter(new CommonTabHandler());
        }

        if (Bukkit.getPluginCommand("account-manager") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("account-manager")).setExecutor(new ManagerCommand());
            Objects.requireNonNull(Bukkit.getPluginCommand("account-manager")).setTabCompleter(new ManagerTabHandler());
        }

        Bukkit.getPluginManager().registerEvents(new StopRestrictedPlayers(), this);
        Bukkit.getPluginManager().registerEvents(new JoinExitEvent(), this);
    }

    @Override
    public void onDisable() {
        PlayerCache.Write();
    }
}
