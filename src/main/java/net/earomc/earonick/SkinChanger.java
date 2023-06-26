package net.earomc.earonick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.earomc.earonick.mojangapi.Skin;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SkinChanger {

    private final Plugin plugin;

    public SkinChanger(Plugin plugin) {
        this.plugin = plugin;
    }

    public void change(@NotNull Player player, Skin skin) {
        GameProfile profile = NMSUtil.getNMSPlayer(player).getProfile();
        PropertyMap pm = profile.getProperties();
        //multimap meaning one key can have multiple values. We remove all textures to replace them with our new skin texture
        pm.removeAll("textures");
        pm.put("textures", skin);
    }

    public void update(Player player) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> update2(player));
        } else update2(player);
    }

    private void update2(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(player);
            onlinePlayer.showPlayer(player);
        }
    }
}
