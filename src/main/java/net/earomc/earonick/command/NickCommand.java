package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.earomc.earonick.config.ConfigWrapper;
import net.earomc.earonick.manager.NickManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 01:48:25
 * (●'◡'●)
 */
public class NickCommand extends Command {

    private final EaroNick plugin;

    public NickCommand(String name, EaroNick plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        ConfigWrapper config = plugin.getConfig();
        ConfigWrapper messageConfig = plugin.getMessageConfig();

        if (!player.hasPermission(config.getString("nick-command.permission"))) {
            player.sendMessage(new TextComponent(messageConfig.color(messageConfig.getString("no-permission"))));
            return;
        }

        if (args.length != 1) {
            sendCommandUsage(player);
            return;
        }

        NickManager nickManager = plugin.getNickManager();

        if (nickManager.isNicked(player)) {
            messageConfig.color(messageConfig.getString("already-nicked"));
            return;
        }

        nickManager.nickPlayer(player, args[0]);

    }

    private void sendCommandUsage(ProxiedPlayer player) {

        for (String currentMessage : plugin.getConfig().getStringList("command-usage")) {
            player.sendMessage(new TextComponent(plugin.getConfig().color(currentMessage)));
        }
    }
}
