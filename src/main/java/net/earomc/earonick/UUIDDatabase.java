package net.earomc.earonick;

import net.earomc.earonick.config.FileWrapper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * @author tiiita_
 * Created on Dezember 15, 2022 | 22:25:08
 * (●'◡'●)
 */
public class UUIDDatabase {

    private final FileWrapper fileWrapper;

    public UUIDDatabase(EaroNick plugin) {
        this.fileWrapper = new FileWrapper(plugin, plugin.getDataFolder(), "player_uuids.txt");
    }

    public UUID getRandomUUID() {

        ArrayList<String> lines = fileWrapper.getLines();

        Random random = new Random();
        int randomInt = random.nextInt(lines.size());
        return UUID.fromString(lines.get(randomInt));
    }
}
