package net.earomc.earonick;

import net.earomc.earonick.command.NickCommand;
import net.earomc.earonick.command.UnnickCommand;
import net.earomc.earonick.config.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class EaroNick extends JavaPlugin {

    private NickManager nickManager;
    private ConfigWrapper config;
    private ConfigWrapper messageConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        saveConfig();

        messageConfig = new ConfigWrapper("messages.yml", getDataFolder(), this);

        nickManager = new NickManager(this);

        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {

        getCommand("nick").setExecutor(new NickCommand(this));
        getCommand("unnick").setExecutor(new UnnickCommand(this));
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();

    }


    public NickManager getNickManager() {
        return nickManager;
    }

    public ConfigWrapper getMessageConfig() {
        return messageConfig;
    }
}
