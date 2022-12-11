package net.earomc.earonick;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.earomc.earonick.command.NickCommand;
import net.earomc.earonick.command.UnnickCommand;
import net.earomc.earonick.config.ConfigWrapper;
import net.earomc.earonick.nick.ChatListener;
import net.earomc.earonick.nick.ConnectionListener;
import net.earomc.earonick.nick.NickManager;
import net.earomc.earonick.skin.PlayerSkin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class EaroNick extends JavaPlugin {

    private NickManager nickManager;
    private ProtocolManager protocolManager;
    private ConfigWrapper messageConfig;
    private PlayerSkin playerSkin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        saveResource("messages.yml", false);

        protocolManager = ProtocolLibrary.getProtocolManager();
        messageConfig = new ConfigWrapper("messages.yml", getDataFolder(), this);
        playerSkin = new PlayerSkin(this);

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

        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new ConnectionListener(this), this);
    }


    public NickManager getNickManager() {
        return nickManager;
    }

    public ConfigWrapper getMessageConfig() {
        return messageConfig;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }
}
