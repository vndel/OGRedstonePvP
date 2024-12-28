package me.drman.redstonepvp.menus;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;
import me.drman.RedPvP;
import me.drman.redstonepvp.containers.MergeAnvilContainer;
import me.drman.redstonepvp.enums.AnvilType;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ConfirmPayment extends FastInv {
    public ConfirmPayment(Player p, AnvilType anvilType) {
        super(9*3, MessagesUtils.format("Confirmation Menu"));
        setItem(11,new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 5).name(MessagesUtils.format("&a&lCONFIRM")).build(), event -> {
            if (anvilType.equals(AnvilType.MERGE)){
                if (RedPvP.getPlugin().getEconomy().has((OfflinePlayer) event.getWhoClicked(),200)) {
                    RedPvP.getPlugin().getEconomy().withdrawPlayer((OfflinePlayer) event.getWhoClicked(), 200);//TODO
                    event.getWhoClicked().closeInventory();
                    openAnvil(p);
                }else{
                    event.getWhoClicked().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cInsufficient funds!"));
                }
            }else if (anvilType.equals(AnvilType.REPAIR)){
                if ( event.getWhoClicked().getInventory().contains(Material.GOLD_INGOT, 5)) {
                    event.getWhoClicked().getItemInHand().setDurability((short) 0);
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().getInventory().removeItem(new ItemStack[]{new ItemBuilder(Material.GOLD_INGOT).amount(5).build()});
                }else{
                    event.getWhoClicked().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cInsufficient resources!"));
                }
            }
        });
        setItem(15,new ItemBuilder(Material.STAINED_GLASS_PANE).data((short) 14).name(MessagesUtils.format("&c&lDECLINE")).build(),event -> {
            event.getWhoClicked().closeInventory();
        });
    }
    public void openAnvil(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        MergeAnvilContainer fakeAnvil = new MergeAnvilContainer(entityPlayer);
        int containerId = entityPlayer.nextContainerCounter();
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(new PacketPlayOutOpenWindow(containerId,
                "minecraft:anvil", new ChatMessage("Repairing"), 0));
        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
        entityPlayer.activeContainer = fakeAnvil;
        entityPlayer.activeContainer.windowId = containerId;
    }
}
