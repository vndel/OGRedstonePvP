package me.drman.redstonepvp.listener;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
        Player player = e.getPlayer();
        RedPlayer miner = RedPvP.getPlugin().getPlayerManager().get(player);
        RedPlayer killer = RedPvP.getPlugin().getPlayerManager().get(player.getKiller());
        if (miner != null&&(miner.isInCombat())||(PluginManager.getInstance().getApi().getArenaManager().isInMatch(player))) {
            List<ItemStack> itemsToDrop = new ArrayList<>();
            miner.clearInventory();
            Bukkit.getServer().getPluginManager().callEvent(new PlayerDeathEvent(player,itemsToDrop,0,""));
        }
        miner.save();
        PluginManager.getInstance().getCombatManager().getManager().remove(miner.getPlayer());
        RedPvP.getPlugin().getPlayerManager().unregister(player);
    }

}