package net.earomc.earonick;

import net.earomc.earonick.config.ConfigWrapper;
import net.earomc.earonick.manager.NickManager;
import net.md_5.bungee.api.plugin.Plugin;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
