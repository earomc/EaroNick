package net.earomc.earonick.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author tiiita_
 * Created on Dezember 12, 2022 | 22:11:25
 * (●'◡'●)
 */
public class SkinChanger {
    private GameProfile profile;
    Player player;

    public SkinChanger(Player player) {
        this.player = player;
        this.profile = ((CraftPlayer) player).getProfile();
    }

    public void change(String value, String signature) {
        PropertyMap pm = this.profile.getProperties();
        pm.remove("textures", pm.get("textures").iterator().next());
        pm.put("textures", new Property(value, signature));
    }


    public void update() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
            onlinePlayer.showPlayer(player);
        }
    }
}
