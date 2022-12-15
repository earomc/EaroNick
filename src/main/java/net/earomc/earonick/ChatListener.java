package net.earomc.earonick;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 13:10:09
 * (●'◡'●)
 *
 * Changes the name of nicked players in chat.
 */

// TODO: Somehow move to EaroCore or a separate EaroChat plugin to have one place where all the chatting is handled.
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
                .replaceAll("%name%", nickManager.getNickName(player))
                .replaceAll("%message%", event.getMessage()));
    }
}
