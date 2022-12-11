package net.earomc.earonick;

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

        //nick player
        player.setCustomName(newName);
        player.setPlayerListName(newName);
        player.setDisplayName(newName);

    }

    public void unnickPlayer(Player player) {
        nickedPlayers.remove(player);

        //unnick player
        String realName = uuidToOldNameMap.get(player.getUniqueId());
        player.setDisplayName(realName);
        player.setPlayerListName(realName);
        player.setDisplayName(realName);


    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }


    /*
    public String getNickName(ProxiedPlayer player) {

    }*/
}
