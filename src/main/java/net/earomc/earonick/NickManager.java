package net.earomc.earonick;

import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 01:49:47
 * (●'◡'●)
 */
public class NickManager {

    private final EaroNick plugin;

    public NickManager(EaroNick plugin) {
        this.plugin = plugin;
    }

    private final HashSet<Player> nickedPlayers = new HashSet<>();
    private HashMap<UUID, String> uuidToOldNameMap = new HashMap<>();

    public void nickPlayer(Player player, String newName) {
        nickedPlayers.add(player);
        uuidToOldNameMap.put(player.getUniqueId(), player.getName());
        FileConfiguration config = plugin.getConfig();
        String playerPrefix = config.getString("nicked-name-prefix-tab").replaceAll("&", "§");

        //nick player
        player.setCustomName(playerPrefix + newName);
        player.setPlayerListName(playerPrefix + newName);
        player.setDisplayName(playerPrefix + newName);
        changeNameTag(player, config.getString("nicked-name-prefix-tag").replaceAll("&", "§" + newName));

    }

    public void unnickPlayer(Player player) {
        nickedPlayers.remove(player);


        //unnick player
        String realName = uuidToOldNameMap.get(player.getUniqueId());
        player.setDisplayName(realName);
        player.setPlayerListName(realName);
        player.setDisplayName(realName);
        changeNameTag(player, realName);
        uuidToOldNameMap.remove(player.getUniqueId());

    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }



    public String getNickName(Player player) {
        return player.getDisplayName();
    }

    private void changeNameTag(Player player, String newName) {
        PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo();


    }
}
