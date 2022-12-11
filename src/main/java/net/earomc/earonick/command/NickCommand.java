package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.earomc.earonick.NickManager;
import net.earomc.earonick.config.ConfigWrapper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 03:21:40
 * (●'◡'●)
 */
public class NickCommand implements CommandExecutor {

    private final EaroNick plugin;

    public NickCommand(EaroNick plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        FileConfiguration config = plugin.getConfig();
        ConfigWrapper messageConfig = plugin.getMessageConfig();

        if (!player.hasPermission(config.getString("nick-command.permission"))) {
            player.sendMessage(messageConfig.color(messageConfig.getString("prefix") + messageConfig.getString("no-permission")));
            return false;
        }

        if (args.length != 1) {
            sendCommandUsage(player);
            return false;
        }

        String prefix = messageConfig.color(messageConfig.getString("prefix"));
        NickManager nickManager = plugin.getNickManager();

        if (nickManager.isNicked(player)) {
            player.sendMessage(prefix + messageConfig.color(messageConfig.getString("already-nicked")));
            return false;
        }

        nickManager.nickPlayer(player, args[0]);
        player.sendMessage(prefix + messageConfig.color(messageConfig.getString("have-been-nicked", "%newNick%", args[0])));

        return true;
    }
    private void sendCommandUsage(Player player) {

        for (String currentMessage : plugin.getConfig().getStringList("nick-command.usage")) {
            player.sendMessage(currentMessage.replaceAll("&", "§"));
        }
    }
}
