package net.earomc.earonick;

import net.earomc.earonick.command.NickCommand;
import net.earomc.earonick.command.UnnickCommand;
import net.earomc.earonick.config.ConfigWrapper;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class EaroNick extends Plugin {

    private NickManager nickManager;
    private ConfigWrapper config;
    private ConfigWrapper messageConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic

        try {
            loadConfig("config.yml");
            loadConfig("messages.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }


        config = new ConfigWrapper("config.yml", getDataFolder(), this);
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
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerCommand(this, new NickCommand("nick", this));
        pluginManager.registerCommand(this, new UnnickCommand("unnick", this));
    }

    private void registerListener() {
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, new ConnectionListener());
    }

    private void loadConfig(String name) throws IOException {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        File file = new File(this.getDataFolder(), name);

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
