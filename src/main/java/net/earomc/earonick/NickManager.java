package net.earomc.earonick;

import com.mojang.authlib.GameProfile;
import net.earomc.earonick.mojangapi.MojangAPI;
import net.earomc.earonick.mojangapi.MojangAPIException;
import net.earomc.earonick.mojangapi.Skin;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tiiita_
 * Created on Dezember 11, 2022 | 01:49:47
 * (●'◡'●)
 */
public class NickManager {

    private final EaroNick plugin;
    private final Skin defaultSkin;

    public NickManager(EaroNick plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        this.defaultSkin = new Skin(config.getString("default-skin.value"), config.getString("default-skin.signature"));
    }

    /*
    we use thread-safe implementations of java.util.Set and java.util.Map because we access them from different threads.
    I just googled "thread-safe HashSet" and "thread-safe HashMap" :P
     */
    private final Set<Player> nickedPlayers = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<UUID, String> playerToRealNameMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, Skin> playerToRealSkinMap = new ConcurrentHashMap<>();


    public static class NickResult {
        public final Player nickedPlayer;
        public final String newName;
        public final boolean fetchSuccessful;
        public final UUID uuid;

        /**
         * @param nickedPlayer The player who's getting nicked.
         * @param newName The player's new nickname.
         * @param uuid The uuid the name and skin is derived from. Not the nicked player's uuid! Null if
         * @param fetchSuccessful Signifies whether a skin could be fetched or a default skin had to be used.
         *      * Which is logically equivalent to whether or not the newName is from a real player.
         *      * True if skin could be fetched (newName player is real) or not (newName player is not real).
         */

        public NickResult(Player nickedPlayer, String newName, @Nullable UUID uuid, boolean fetchSuccessful) {
            this.newName = newName;
            this.nickedPlayer = nickedPlayer;
            this.fetchSuccessful = fetchSuccessful;
            this.uuid = uuid;
        }
    }

    /**
     * @param player  player that you would like to nick.
     * @param newName the players new nickname.
     * @return Returns
     */
    public NickResult nickPlayer(Player player, String newName) {
        SkinChanger skinChanger = plugin.getSkinChanger();
        playerToRealNameMap.put(player.getUniqueId(), player.getDisplayName());
        playerToRealSkinMap.put(player.getUniqueId(), Skin.fromPlayer(player));

        Skin skin;
        boolean requestSuccessful;
        UUID uuid = null;
        try {
            uuid = MojangAPI.requestUUID(newName);
            skin = MojangAPI.requestSkin(uuid);
            requestSuccessful = true;
        } catch (MojangAPIException e) {
            try {
                uuid = plugin.getUuidDatabase().getRandomUUID();
                if (uuid == null) {
                    throw new MojangAPIException("Couldn't get UUID from Database", new NullPointerException());
                }
                skin = MojangAPI.requestSkin(uuid);
            } catch (MojangAPIException e1) {
                skin = defaultSkin;
            }
            requestSuccessful = false;
        }

        changeName(player, newName);

        skinChanger.change(player, skin);
        skinChanger.update(player);

        nickedPlayers.add(player);
        NickResult nickResult = new NickResult(player, newName, uuid, requestSuccessful);
        Bukkit.getPluginManager().callEvent(new PlayerNickEvent(player, nickResult));
        return nickResult;
    }


    public NickResult randomNickPlayer(Player player) throws MojangAPIException {
        UUID randomUUID = plugin.getUuidDatabase().getRandomUUID();

        if (randomUUID == null) {
            throw new MojangAPIException("Couldn't get UUID from Database", new NullPointerException());
        }
        String requestedName = MojangAPI.requestName(randomUUID);
        return nickPlayer(player, requestedName);
    }

    public CompletableFuture<NickResult> randomNickPlayerAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return randomNickPlayer(player);
            } catch (MojangAPIException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<NickResult> nickPlayerAsync(Player player, String newName) {
        return CompletableFuture.supplyAsync(() -> nickPlayer(player, newName));
    }


    public void unnickPlayer(Player player) {
        SkinChanger skinChanger = plugin.getSkinChanger();
        String realName = playerToRealNameMap.get(player.getUniqueId());

        changeName(player, realName);

        skinChanger.change(player, playerToRealSkinMap.get(player.getUniqueId()));
        skinChanger.update(player);


        nickedPlayers.remove(player);
        playerToRealNameMap.remove(player.getUniqueId());
        playerToRealSkinMap.remove(player.getUniqueId());
    }

    public boolean isNicked(Player player) {
        return nickedPlayers.contains(player);
    }


    public String getNickName(Player player) {
        return player.getDisplayName();
    }

    public void changeName(Player player, String newName) {
        player.setDisplayName(newName);
        player.setPlayerListName(newName);
        player.setDisplayName(newName);

        EntityPlayer nmsPlayer = NMSUtil.getNMSPlayer(player);

        //MODIFY THE PLAYER'S GAME PROFILE VIA REFLECTION
        GameProfile gameProfile = nmsPlayer.getProfile();
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
            if (currentPlayer == player) continue;
            EntityPlayer currentNMSPlayer = NMSUtil.getNMSPlayer(currentPlayer);
            PlayerConnection currentPlayerConnection = currentNMSPlayer.playerConnection;

            //"REMOVE" THE PLAYER FOR EACH ONLINE PLAYER
            currentPlayerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, nmsPlayer));
            currentPlayerConnection.sendPacket(new PacketPlayOutEntityDestroy(player.getEntityId()));

            //RE-ADD THE PLAYER
            currentPlayerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, nmsPlayer));
            currentPlayerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(nmsPlayer));
        }
    }


    public Set<Player> getNickedPlayers() {
        return nickedPlayers;
    }
}