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

    private UUID uuid;
    private EntityPlayer entityPlayer;
    private GameProfile profile;

    public SkinChanger(UUID uuid) {
        this.uuid = uuid;
        this.profile = ((CraftPlayer) Bukkit.getPlayer(uuid)).getProfile();
        this.entityPlayer = ((CraftPlayer)Bukkit.getPlayer(uuid)).getHandle();
    }

    public void change(String value, String signature) {
        PropertyMap pm = this.profile.getProperties();
        pm.remove("textures", pm.get("textures").iterator().next());
        pm.put("textures", new Property(value, signature));
    }


    public void update() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer((Player) entityPlayer);
            onlinePlayer.showPlayer((Player) entityPlayer);
        }

        /*Player modifiedPlayer = Bukkit.getPlayer(this.uuid);
        PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entityPlayer);
        PacketPlayOutEntityDestroy destroyEntity = new PacketPlayOutEntityDestroy(new int[] {Bukkit.getPlayer(this.uuid).getEntityId()});
        PacketPlayOutNamedEntitySpawn addNamed = new PacketPlayOutNamedEntitySpawn(this.entityPlayer);
        PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entityPlayer);
        PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(this.entityPlayer.dimension, this.entityPlayer.getWorld().getDifficulty(), this.entityPlayer.getWorld().getWorldData().getType(), this.entityPlayer.playerInteractManager.getGameMode());
        for (Player p: Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(removeInfo);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(destroyEntity);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(addNamed);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(addInfo);
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(respawn);
            p.hidePlayer(modifiedPlayer);
            p.showPlayer(modifiedPlayer);
        }*/
    }

}
