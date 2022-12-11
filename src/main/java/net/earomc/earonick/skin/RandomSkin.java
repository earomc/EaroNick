package net.earomc.earonick.skin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.earomc.earonick.EaroNick;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 17:59:28
 * (●'◡'●)
 */
public class RandomSkin {
    private final EaroNick plugin;

    public RandomSkin(EaroNick plugin) {
        this.plugin = plugin;
    }


    public void changeTo(Player player) {
        GameProfile gameProfile = ((CraftPlayer)player).getHandle().getProfile();
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle()));

        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", get());

        connection.sendPacket(new PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)player).getHandle()));

    }

    public Property get() {

        FileConfiguration config = plugin.getConfig();
        Random random = new Random();
        List<String> skins = config.getStringList("skins");
        int randomNumber = random.nextInt(skins.size());

        return new Property("textures", skins.get(randomNumber));
    }
}