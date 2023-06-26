package net.earomc.earonick;

import net.earomc.earonick.config.FileWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 * Represents a file of player UUID strings separated with a \n newline.
 * </p>
 * <p>
 * This allows to choose random nicknames and skins for when players
 * don't specify a nickname using the /nick command.
 * </p>
 */
public class UUIDDatabase {

    private final FileWrapper uuidFile;

    public UUIDDatabase(EaroNick plugin) {
        this.uuidFile = new FileWrapper(plugin, plugin.getDataFolder(), "player_uuids.txt");
    }

    @Nullable
    public UUID getRandomUUID() {
        ArrayList<String> lines = uuidFile.getLines();
        if (lines == null || lines.isEmpty()) {
            return null;
        }
        return UUID.fromString(lines.get(new Random().nextInt(lines.size())));
    }
}
