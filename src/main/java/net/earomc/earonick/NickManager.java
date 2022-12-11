package net.earomc.earonick;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 01:49:47
 * (●'◡'●)
 */
public class NickManager {

    private final EaroNick plugin;

    public NickManager(EaroNick plugin) {
        this.plugin = plugin;
    }

    private final HashSet<Player> nickedPlayers = new HashSet<>();
    private final HashMap<UUID, String> uuidToOldNameMap = new HashMap<>();

    public void nickPlayer(Player player, String newName) {
        nickedPlayers.add(player);
        uuidToOldNameMap.put(player.getUniqueId(), player.getName());
        FileConfiguration config = plugin.getConfig();
        String playerPrefix = config.getString("nicked-name-prefix-tab").replaceAll("&", "§");

        //nick player
        String finalNewName = playerPrefix + newName;
        player.setCustomName(finalNewName);
        player.setPlayerListName(finalNewName);
        player.setDisplayName(finalNewName);


        changeNameTag(player, finalNewName);
    }

    public void unnickPlayer(Player player) {
        nickedPlayers.remove(player);


        //unnick player
        String realName = uuidToOldNameMap.get(player.getUniqueId());
        player.setDisplayName(realName);
        player.setPlayerListName(realName);
        player.setDisplayName(realName);
        uuidToOldNameMap.remove(player.getUniqueId());

    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }


    public String getNickName(Player player) {
        return player.getDisplayName();
    }

    public void changeNameTag(Player player, String newName){
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl == player) continue;
            //REMOVES THE PLAYER
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
            //CHANGES THE PLAYER'S GAME PROFILE
            GameProfile gp = ((CraftPlayer) player).getProfile();
            try {
                Field nameField = GameProfile.class.getDeclaredField("name");
                nameField.setAccessible(true);

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

                nameField.set(gp, newName);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new IllegalStateException(ex);
            }
            //ADDS THE PLAYER
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) player).getHandle()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));
            ((CraftPlayer)pl).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle()));
        }
    }
}