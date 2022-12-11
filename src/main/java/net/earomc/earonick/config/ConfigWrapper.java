package net.earomc.earonick.config;

import net.earomc.earonick.EaroNick;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * @author NieGestorben, tiiita_
 * Created on Juli 29, 2022 | 18:31:59
 * (●'◡'●)
 */

public class ConfigWrapper {
    private final EaroNick plugin;

    private YamlConfiguration fileConfiguration;
    private final File file;

    public ConfigWrapper(String name, File path, EaroNick plugin) {
        this.plugin = plugin;
        file = new File(path, name);
        if (!file.exists()) {
            path.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().log(Level.SEVERE, "There was an error creating the config!");
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            fileConfiguration.load(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "There was an error reloading the config!");
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }


    public String color(String path) {
        return ChatColor.translateAlternateColorCodes('&', path);
    }


    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public String getString(String path, String placeholder, String replacement) {
        return fileConfiguration.getString(path).replaceAll(placeholder, replacement);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public void setString(String path, String value) {
        fileConfiguration.set(path, value);
    }

    public void setBoolean(String path, boolean value) {
        fileConfiguration.set(path, value);
    }

    public void setInt(String path, int value) {
        fileConfiguration.set(path, value);
    }

    public void setDouble(String path, double value) {
        fileConfiguration.set(path, value);
    }
}