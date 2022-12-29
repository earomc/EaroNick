package net.earomc.earonick;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerNickEvent extends PlayerEvent {

    private final static HandlerList handlerList = new HandlerList();
    private final NickManager.NickResult nickResult;

    public PlayerNickEvent(Player who, NickManager.NickResult nickResult) {
        super(who);
        this.nickResult = nickResult;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public NickManager.NickResult getNickResult() {
        return nickResult;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
