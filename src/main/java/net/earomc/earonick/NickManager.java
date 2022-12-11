package net.earomc.earonick;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 01:49:47
 * (●'◡'●)
 */
public class NickManager {

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
