package net.earomc.earonick;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 13:14:29
 * (●'◡'●)
 */
public class ConnectionListener implements Listener {

    private final EaroNick plugin;

    public ConnectionListener(EaroNick plugin) {
        this.plugin = plugin;
    }



    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NickManager nickManager = plugin.getNickManager();

        for (Player nickedPlayer : nickManager.getNickedPlayers()) {
            if (nickedPlayer == player) continue;
            if (nickedPlayer == null) continue;


            if (nickManager.getNickName(nickedPlayer).equalsIgnoreCase(player.getName())) {
                nickedPlayer.kickPlayer(plugin.getConfig().getString("player-with-nick-joined").replaceAll("%newLine%", "\n"));
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        NickManager nickManager = plugin.getNickManager();

        if (nickManager.isNicked(player))
            nickManager.unnickPlayer(player);
    }
}
