package me.drman.redstonepvp.commands;

import net.brcdev.gangs.GangsPlusApi;
import net.brcdev.gangs.gang.Gang;
import me.drman.RedPvP;
import me.drman.redstonepvp.utils.InventoryUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;

public class GangChestCommand {

    @Command("gangchest")
    public void onGangChest(Player player) {
        if (!GangsPlusApi.isInGang(player)) {
            player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + "&cYou have to be in a gang to use this command!"));
            return;
        }

        Gang gang = GangsPlusApi.getPlayersGang(player);
        Inventory inventory = Bukkit.createInventory(null,6*9,"Gang Chest");

        if (RedPvP.getPlugin().getDataProvider().registeredGang(gang)) {
            String invString = RedPvP.getPlugin().getDataProvider().getGangInventory(gang.getName());
            inventory = createGangInventory(gang, invString);
        }else{
            RedPvP.getPlugin().getDataProvider().registerGang(gang,InventoryUtils.toStorage(inventory,gang.getName()));
        }

        player.openInventory(inventory);


    }

    private Inventory createGangInventory(Gang gang, String invString) {
        ItemStack[] items = InventoryUtils.fromStorage(invString);
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Gang Chest");
        inventory.setContents(items);
        return inventory;
    }
}
