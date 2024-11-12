package me.drman.redstonepvp.listener;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        RedPlayer player = RedPvP.getPlugin().getPlayerManager().get(e.getEntity());
        Location spawn = LocationsUtils.deParseLocation(ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().getString("locations.spawn-point"));

        if (!player.hasData()) return;
        if (player.inDuel()) {
            player.clearInventory();
            player.getPlayer().closeInventory();
            player.respawn(spawn);
            return;
        }
        if ((e.getEntity() instanceof Player) && (e.getEntity().getKiller() instanceof Player)) {
            RedPlayer killer = RedPvP.getPlugin().getPlayerManager().get(e.getEntity().getKiller());
            if (player.isInCombat()) {
                PluginManager.getInstance().getCombatManager().getManager().remove(player.getPlayer());
                e.getEntity().sendMessage(MessagesUtils.getMessage("CombatLog.combat-quit"));
            }
            player.clearInventory();
            player.getPlayer().closeInventory();
            player.respawn(spawn);
            e.setDeathMessage(null);
            if (killer.getPlayer().equals(player.getPlayer())) return;
            int streak = (int) player.getStreak();
            player.addDeaths(1);
            player.setStreak(0);
            killer.addKills(1);
            killer.addStreak(1);
            RedPvP.getPlugin().getEconomy().depositPlayer(killer.getPlayer(), killer.getCurrentTier().getBooster());
            if (killer.getStreak() % 5 == 0) {
                Bukkit.broadcastMessage(RedPvP.getPlugin().getPrefix()+MessagesUtils.format("&d"+killer.getPlayer().getName()+" &fhas reached killstreaks of &d"+killer.getStreak()));
            }
            if (streak>=10){
                Bukkit.broadcastMessage(RedPvP.getPlugin().getPrefix()+MessagesUtils.format("&b"+player.getPlayer().getName()+" &fhas lost their killstreaks of &d"+streak));
            }
            if (killer.isInCombat()) {
                PluginManager.getInstance().getCombatManager().getManager().remove(killer.getPlayer());
                killer.getPlayer().sendMessage(MessagesUtils.getMessage("CombatLog.combat-quit"));
            }
            player.equip();
        }
    }
}