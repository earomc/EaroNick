package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.earomc.earonick.NickManager;
import net.earomc.earonick.config.ConfigWrapper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 02:14:17
 * (●'◡'●)
 */
public class UnnickCommand extends Command {

    private final EaroNick plugin;

    public UnnickCommand(String name, EaroNick plugin) {
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

        if (!player.hasPermission(config.getString("unnick-command.permission"))) {
            player.sendMessage(new TextComponent(messageConfig.color(messageConfig.getString("prefix") + " " + messageConfig.getString("no-permission"))));
            return;
        }

        if (args.length != 0) {
            sendCommandUsage(player);
            return;
        }

        NickManager nickManager = plugin.getNickManager();

        String coloredPrefixWithSpace = messageConfig.color(messageConfig.getString("prefix") + " ");
        if (!nickManager.isNicked(player)) {
            player.sendMessage(new TextComponent(coloredPrefixWithSpace + messageConfig.color(messageConfig.getString("not-nicked"))));
            return;
        }

        nickManager.unnickPlayer(player);
        player.sendMessage(new TextComponent(coloredPrefixWithSpace + messageConfig.color(messageConfig.getString("have-been-unnicked"))));
    }

    private void sendCommandUsage(ProxiedPlayer player) {

        for (String currentMessage : plugin.getConfig().getStringList("unnick-command.usage")) {
            player.sendMessage(new TextComponent(plugin.getConfig().color(currentMessage)));
        }
    }
}
