package me.drman.redstonepvp.listener;

import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer());
        e.setCancelled(!redPlayer.isBuilding());
    }
    @EventHandler
    public void onBreak(BlockPlaceEvent e){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer());
        e.setCancelled(!redPlayer.isBuilding());
    }
}
