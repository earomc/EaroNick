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
 * Created on Dezember 11, 2022 | 03:24:25
 * (●'◡'●)
 */
public class UnnickCommand implements CommandExecutor {
    private final EaroNick plugin;

    public UnnickCommand(EaroNick plugin) {
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

        if (!player.hasPermission(config.getString("unnick-command.permission"))) {
            player.sendMessage(messageConfig.color(messageConfig.getString("prefix") + messageConfig.getString("no-permission")));
            return false;
        }

        if (args.length != 0) {
            sendCommandUsage(player);
            return false;
        }

        NickManager nickManager = plugin.getNickManager();

        String prefix = messageConfig.color(messageConfig.getString("prefix"));
        if (!nickManager.isNicked(player)) {
            player.sendMessage(prefix + messageConfig.color(messageConfig.getString("not-nicked")));
            return false;
        }

        nickManager.unnickPlayer(player);
        player.sendMessage(prefix + messageConfig.color(messageConfig.getString("have-been-unnicked")
                .replaceAll("%newLine%", "\n" + prefix)));
        return true;
    }

    private void sendCommandUsage(Player player) {

        for (String currentMessage : plugin.getConfig().getStringList("unnick-command.usage")) {
            player.sendMessage(currentMessage.replaceAll("&", "§"));
        }
    }
}
