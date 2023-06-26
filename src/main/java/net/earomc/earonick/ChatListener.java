package net.earomc.earonick;

import net.earomc.earocore.chat.EaroChatEvent;
import net.earomc.earocore.rank.Rank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private final NickManager nickManager;

    public ChatListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(EaroChatEvent event) {
        if (!nickManager.isNicked(event.getPlayer())) {
            return;
        }
        event.setName(event.getName());
        event.setRank(Rank.PLAYER);
    }
}
