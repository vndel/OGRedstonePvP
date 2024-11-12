package me.drman.redstonepvp.menus;

import fr.mrmicky.fastinv.FastInv;
import me.drman.RedPvP;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DropPartyMenu extends FastInv {
    private Player player;
    public DropPartyMenu(Player p) {
        super(9*6,"DropParty Editor");
        this.player = p;
        for (ItemStack i : PluginManager.getInstance().getDropPartyManager().getItems()){
            addItem(i);
        }
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(false);
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&aItems saved successfully"));
        PluginManager.getInstance().getDropPartyManager().getItems().clear();
        for (ItemStack i : event.getInventory().getContents()){
            if (i != null && !i.getType().equals(Material.AIR)) {
                PluginManager.getInstance().getDropPartyManager().getItems().add(i);
            }

        }
    }
}
