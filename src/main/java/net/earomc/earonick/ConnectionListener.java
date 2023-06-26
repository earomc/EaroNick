package net.earomc.earonick;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final NickManager nickManager;

    public ConnectionListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Player nickedPlayer : nickManager.getNickedPlayers()) {
            if (nickManager.getNickName(nickedPlayer).equalsIgnoreCase(player.getName())) {
                nickedPlayer.kickPlayer("Kicked because a player with your nick name joined.");
            }
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (nickManager.isNicked(player)) {
            nickManager.unnickPlayer(player);
        }
    }
}
