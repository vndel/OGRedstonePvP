package me.drman.redstonepvp.listener;

import me.drman.Axes;
import me.drman.redstonepvp.managers.PluginManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemDrop implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if (Axes.getPlugin().getAxesManager().isAxe(e.getItemDrop().getItemStack())){
            e.setCancelled(true);
        }
    }

}