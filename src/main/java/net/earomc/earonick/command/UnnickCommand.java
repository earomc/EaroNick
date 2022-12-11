package net.earomc.earonick.command;

import net.earomc.earonick.EaroNick;
import net.md_5.bungee.api.CommandSender;
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

    }
}
