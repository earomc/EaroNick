package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.earomc.earonick.config.ConfigWrapper;
import net.earomc.earonick.NickManager;
import net.earomc.earonick.mojangapi.MojangAPIException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.function.BiConsumer;

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

        NickManager nickManager = plugin.getNickManager();

        String nickedWithDefaultSkinWarning = messageConfig.getString("nicked-with-default-skin-warning")
                .replaceAll("%newLine%", "\n" + prefix);
        String nickSuccessfulMessage = messageConfig.getString("have-been-nicked").replaceAll("%newLine%", "\n" + prefix);

        String errorMessage = messageConfig.getString("error");

        BiConsumer<NickManager.NickResult, Throwable> handleNickAttemptAction = (nickResult, throwable) -> {
            if (throwable != null) {
                System.err.println(throwable.getMessage());
                throwable.printStackTrace();
                player.sendMessage(prefix + errorMessage.replaceAll("%error%", throwable.getCause().getClass().getName() + throwable.getMessage()));
            } else {
                player.sendMessage(prefix + nickSuccessfulMessage.replaceAll("%newNick%", nickResult.newName));
                if (!nickResult.fetchSuccessful) {
                    player.sendMessage(prefix + nickedWithDefaultSkinWarning.replaceAll("%newNick%", nickResult.newName));
                }
            }
        };

        switch (args.length) {
            case 0: {
                if (nickManager.isNicked(player)) {
                    player.sendMessage(prefix + messageConfig.getString("already-nicked"));
                    return true;
                }
                nickManager.randomNickPlayerAsync(player).whenComplete(handleNickAttemptAction);
                break;
            }
            case 1:
                if (nickManager.isNicked(player)) {
                    player.sendMessage(prefix + messageConfig.getString("already-nicked"));
                    return true;
                }
                String newNick = args[0];

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayer.getName().equalsIgnoreCase(newNick)) {
                        player.sendMessage(prefix + plugin.getMessageConfig().getString("nick-already-online"));
                        return true;
                    }
                }

                if (newNick.length() >= 16) {
                    player.sendMessage(prefix + messageConfig.getString("nickname-too-long"));
                    return true;
                }
                nickManager.nickPlayerAsync(player, newNick).whenComplete(handleNickAttemptAction);
                break;
        }

        return true;
    }
}
