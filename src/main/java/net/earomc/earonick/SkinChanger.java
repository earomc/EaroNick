package net.earomc.earonick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * @author tiiita_
 * Created on Dezember 12, 2022 | 22:11:25
 * (●'◡'●)
 */
public class SkinChanger {

    public Property getPlayerProperty(String name) {


    }
    public void change(Player player, String value, String signature) {

        GameProfile profile = ((CraftPlayer)player).getHandle().getProfile();
        PropertyMap pm = profile.getProperties();
        pm.remove("textures", pm.get("textures").iterator().next());
        pm.put("textures", new Property(value, signature));
    }


    public void update(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
            onlinePlayer.showPlayer(player);
        }
    }
}
