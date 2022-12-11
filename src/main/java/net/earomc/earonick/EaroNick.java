package net.earomc.earonick;

import net.earomc.earonick.command.NickCommand;
import net.earomc.earonick.command.UnnickCommand;
import net.earomc.earonick.config.ConfigWrapper;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public final class EaroNick extends Plugin {

    private NickManager nickManager;
    private ConfigWrapper config;
    private ConfigWrapper messageConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //load config.yml
        config = new ConfigWrapper("config.yml", getDataFolder(), this);
        messageConfig = new ConfigWrapper("messages.yml", getDataFolder(), this);

        nickManager = new NickManager();

        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerCommand(this, new NickCommand("nick", this));
        pluginManager.registerCommand(this, new UnnickCommand("unnick", this));
    }

    private void registerListener() {
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, new ConnectionListener());
    }
    public NickManager getNickManager() {
        return nickManager;
    }
    public ConfigWrapper getConfig() {
        return config;
    }

    public ConfigWrapper getMessageConfig() {
        return messageConfig;
    }
}
