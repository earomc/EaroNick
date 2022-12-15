package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.earomc.earonick.config.ConfigWrapper;
import net.earomc.earonick.NickManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

    //The boolean returned by onCommand method indicates whether the usage message given in the plugin.yml should be sent or not.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be executed by a player!");
            return false;
        }
        Player player = (Player) sender;

        ConfigWrapper messageConfig = plugin.getMessageConfig();
        String prefix = messageConfig.getString("prefix");

        if (!player.hasPermission(command.getPermission())) {
            player.sendMessage(prefix + messageConfig.getString("no-permission"));
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        NickManager nickManager = plugin.getNickManager();

        if (nickManager.isNicked(player)) {
            player.sendMessage(prefix + messageConfig.getString("already-nicked"));
            return true;
        }

        String newNick = args[0];
        if (newNick.length() >= 16) {
            player.sendMessage(prefix + messageConfig.getString("nickname-too-long"));
            return true;
        }

        String nickedWithDefaultSkinWarning = messageConfig.getString("nicked-with-default-skin-warning")
                .replaceAll("%newLine%", "\n" + prefix);
        String nickSuccessfulMessage = messageConfig.getString("have-been-nicked").replaceAll("%newLine%", "\n" + prefix);

        String errorMessage = messageConfig.getString("error");
        nickManager.nickPlayerAsync(player, newNick).whenComplete((fetchedSkin, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                player.sendMessage(prefix + errorMessage.replaceAll("%error%", throwable.getCause().getClass().getName() + throwable.getMessage()));
            } else {
                player.sendMessage(prefix + nickSuccessfulMessage.replaceAll("%newNick%", newNick));
                if (!fetchedSkin) {
                    player.sendMessage(prefix + nickedWithDefaultSkinWarning.replaceAll("%newNick%", newNick));
                }
            }
        });

        return true;
    }
}
