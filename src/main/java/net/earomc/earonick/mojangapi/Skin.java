package net.earomc.earonick.mojangapi;

import com.mojang.authlib.properties.Property;
import net.earomc.earonick.NMSUtil;
import net.earomc.earonick.mojangapi.requests.ProfileRequest;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the (Skin) "textures" property found when requesting a Player-/GameProfile via the Mojang API.
 */
public class Skin extends Property {
    public Skin(String value) {
        super("textures", value); // name, value
    }

    public Skin(String value, String signature) {
        super("textures", value, signature); // name, value, signature
    }

    public static Skin fromPlayer(@NotNull Player player) {
        /*
        PropertiesMap is based on a MultiMap therefore one key (in this case "textures") can have multiple values therefore we need to
        "Iterate" through the "texture" property even though we know it only has one entry in this case.
         */
        Property textures = NMSUtil.getNMSPlayer(player).getProfile().getProperties().get("textures").stream().findFirst().orElse(null);
        if (textures == null) throw new IllegalStateException("Player has no skin texture?!");
        return new Skin(textures.getValue(), textures.getSignature());
    }

    public static Skin fromProfileRequest(ProfileRequest.Result profileRequestResult) {
        return new Skin(profileRequestResult.value, profileRequestResult.signature);
    }
}
