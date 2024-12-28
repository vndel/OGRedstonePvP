package me.drman.redstonepvp.utils;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarUtils {
    public static void sendActionBar(Player p, String string) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + string.replace("&", "§") + "\"}"), (byte) 2);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
}