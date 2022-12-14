package net.earomc.earonick.nick;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.earomc.earonick.EaroNick;
import net.earomc.earonick.SkinChanger;
import net.minecraft.server.v1_8_R3.*;
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
    private final HashMap<UUID, Property> uuidToPropertyMap = new HashMap<>();


    /**
     *
     * @param player player that you would like to nick.
     * @param newName the players new nickname.
     * @return true if it works, false if there was an error.
     */
    public boolean nickPlayer(Player player, String newName) throws Exception {
        FileConfiguration config = plugin.getConfig();
        nickedPlayers.add(player);
        uuidToOldNameMap.put(player.getUniqueId(), player.getName());
        uuidToPropertyMap.put(player.getUniqueId(), ((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures").stream().findFirst().orElse(null));

        //works if offline player exists
        SkinChanger skinChanger = plugin.getSkinChanger();

        if (skinChanger.getPlayerProperty(newName) == null) {
            skinChanger.change(player, config.getString("default-skin.value"), config.getString("default-skin.signature"));
            skinChanger.update(player);
            return true;
        }


        Property targetProperty = skinChanger.getPlayerProperty(newName);
        skinChanger.change(player, targetProperty.getValue(), targetProperty.getSignature());
        skinChanger.update(player);

        return true;
    }

    public void unnickPlayer(Player player) {

        //unnick player
        String realName = uuidToOldNameMap.get(player.getUniqueId());
        player.setDisplayName(realName);
        player.setPlayerListName(realName);
        player.setDisplayName(realName);

        changeNameTag(player, realName);

        SkinChanger skinChanger = new SkinChanger(player);

        skinChanger.change(uuidToPropertyMap.get(player.getUniqueId()).getValue(), uuidToPropertyMap.get(player.getUniqueId()).getSignature());
        skinChanger.update();


        nickedPlayers.remove(player);
        uuidToOldNameMap.remove(player.getUniqueId());
        uuidToPropertyMap.remove(player.getUniqueId());

    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }


    public String getNickName(Player player) {
        return player.getDisplayName();
    }

    public void changeNameTag(Player playerToBeChanged, String newName) {
        EntityPlayer nmsPlayerToBeChanged = getNMSPlayer(playerToBeChanged);

        //MODIFY THE PLAYER'S GAME PROFILE
        GameProfile gameProfile = nmsPlayerToBeChanged.getProfile();
        try {
            Field nameField = GameProfile.class.getDeclaredField("name");
            nameField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(nameField, nameField.getModifiers() & ~Modifier.FINAL);

            nameField.set(gameProfile, newName);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
            throw new IllegalStateException(ex);
        }

        for (Player currentPlayer : Bukkit.getOnlinePlayers()) {
            if (currentPlayer == playerToBeChanged) continue;
            EntityPlayer currentNMSPlayer = getNMSPlayer(currentPlayer);
            PlayerConnection currentPlayerConnection = currentNMSPlayer.playerConnection;
            //"REMOVE" THE PLAYER FOR EACH ONLINE PLAYER
            currentPlayerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, nmsPlayerToBeChanged));
            currentPlayerConnection.sendPacket(new PacketPlayOutEntityDestroy(playerToBeChanged.getEntityId()));


            //RE-ADD THE PLAYER
            currentPlayerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, nmsPlayerToBeChanged));
            currentPlayerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(nmsPlayerToBeChanged));
        }
    }

    private EntityPlayer getNMSPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}