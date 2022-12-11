package net.earomc.earonick;

import net.md_5.bungee.api.connection.ProxiedPlayer;
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

    private final HashSet<ProxiedPlayer> nickedPlayers = new HashSet<>();
    public void nickPlayer(ProxiedPlayer player, String newName) {
        nickedPlayers.add(player);
    }

    public void unnickPlayer(ProxiedPlayer player) {
        nickedPlayers.remove(player);
    }

    public boolean isNicked(ProxiedPlayer player) {
        return nickedPlayers.contains(player);
    }


    /*
    public String getNickName(ProxiedPlayer player) {

    }*/
}
