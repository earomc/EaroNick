package net.earomc.earonick;

import net.earomc.earonick.command.NickCommand;
import net.earomc.earonick.command.UnnickCommand;
import net.earomc.earonick.config.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public final class EaroNick extends JavaPlugin {

    private NickManager nickManager;
    private ConfigWrapper messageConfig;
    private SkinChanger skinChanger;
    private UUIDDatabase uuidDatabase;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        saveResource("messages.yml", false);
        saveResource("player_uuids.txt", false);

        uuidDatabase = new UUIDDatabase(this);
        skinChanger = new SkinChanger(this);
        messageConfig = new ConfigWrapper("messages.yml", getDataFolder(), this);

        nickManager = new NickManager(this);

        registerCommands();
        registerListener();
    }

    private void registerCommands() {
        getCommand("nick").setExecutor(new NickCommand(this));
        getCommand("unnick").setExecutor(new UnnickCommand(this));
    }

    private void registerListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ConnectionListener(nickManager), this);
    }


    public NickManager getNickManager() {
        return nickManager;
    }

    public ConfigWrapper getMessageConfig() {
        return messageConfig;
    }

    public SkinChanger getSkinChanger() {
        return skinChanger;
    }

    public UUIDDatabase getUuidDatabase() {
        return uuidDatabase;
    }
}
