package net.earomc.earonick.nick;

import net.earomc.earonick.EaroNick;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 13:10:09
 * (●'◡'●)
 *
 * Changes the name of nicked players in chat.
 */
public class ChatListener implements Listener {
    private final EaroNick plugin;

    public ChatListener(EaroNick plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        NickManager nickManager = plugin.getNickManager();
        FileConfiguration config = plugin.getConfig();
        if (!nickManager.isNicked(player)) {
            return;
        }

        event.setFormat(config.getString("nick-chat-format")
                .replaceAll("&", "§")
                .replaceAll("%nickname%", nickManager.getNickName(player))
                .replaceAll("%message%", event.getMessage()));
    }
}
