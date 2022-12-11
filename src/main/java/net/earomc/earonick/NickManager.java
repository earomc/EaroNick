package net.earomc.earonick;

import org.bukkit.entity.Player;

import java.util.HashSet;
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
    public void nickPlayer(Player player, String newName) {
        nickedPlayers.add(player);

        //nick player
        player
    }

    public void unnickPlayer(Player player) {
        nickedPlayers.remove(player);

        //unnick player

    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }


    /*
    public String getNickName(ProxiedPlayer player) {

    }*/
}
