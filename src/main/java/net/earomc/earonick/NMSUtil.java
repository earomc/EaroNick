package net.earomc.earonick;

import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NMSUtil {
    public static EntityPlayer getNMSPlayer(@NotNull Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
