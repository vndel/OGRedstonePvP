package me.drman.redstonepvp.listener;

import net.brcdev.gangs.GangsPlusApi;
import net.brcdev.gangs.gang.Gang;
import me.drman.RedPvP;
import me.drman.redstonepvp.utils.InventoryUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (!GangsPlusApi.isInGang(player)) {
            return;
        }

        if (e.getInventory().getTitle().equals("Gang Chest")) {
            Gang gang = GangsPlusApi.getPlayersGang(player);
            try {
                RedPvP.getPlugin().getDataProvider().setGang(gang, InventoryUtils.toStorage(e.getInventory(),gang.getName()));
                player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + "&aGang items saved!"));
            } catch (Exception ex) {
                player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + "&cError saving gang items!"));
                ex.printStackTrace(); // For debugging
            }
        }
    }

}
