package xyz.liamsho.mdauth;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.liamsho.mdauth.commands.CommonCommand;
import xyz.liamsho.mdauth.commands.tabhandler.CommonTabHandler;
import xyz.liamsho.mdauth.eventlisteners.JoinExitEvent;
import xyz.liamsho.mdauth.eventlisteners.StopRestrictedPlayers;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MDAuth extends JavaPlugin {

    public static Plugin MDAuthPlugin;
    public static Logger MDAuthLogger;
    public static FileConfiguration MDAuthConfiguration;
    public static LuckPerms LuckPermsApi;

    @Override
    public void onEnable() {
        MDAuthPlugin = this;

        MDAuthLogger = getLogger();

        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            MDAuthLogger.log(Level.SEVERE, "Configuration file does not exist!");
            getServer().shutdown();
            return;
        }

        MDAuthConfiguration = getConfig();

        if (Bukkit.getPluginCommand("account") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("account")).setExecutor(new CommonCommand());
            Objects.requireNonNull(Bukkit.getPluginCommand("account")).setTabCompleter(new CommonTabHandler());
        }

        Bukkit.getPluginManager().registerEvents(new StopRestrictedPlayers(), this);
        Bukkit.getPluginManager().registerEvents(new JoinExitEvent(), this);

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            LuckPermsApi = provider.getProvider();
        }

        MDAuthLogger.info("MDAuth Enabled.");
    }
}
