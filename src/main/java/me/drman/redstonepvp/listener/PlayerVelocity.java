package me.drman.redstonepvp.listener;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.AxeTarget;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class PlayerVelocity implements Listener {
    private int duration = PluginManager.getInstance().getConfigurationManager().get(ConfigurationType.DEFAULT).getInt("combat-log.duration");
    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        if (!(e.getPlayer().getKiller() instanceof LivingEntity)) return;
        if (e.getPlayer().equals(e.getPlayer().getKiller())) return;
        if (e.getVelocity().equals(new Vector(0,0,0))) return;
        if (PluginManager.getInstance().getApi().getArenaManager().isInMatch(e.getPlayer())) return;
        RedPlayer attacker = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer().getKiller());

        RedPlayer player = RedPvP.getPlugin().getPlayerManager().get((Player) e.getPlayer());
        if (!player.isInCombat()) {
            e.getPlayer().sendMessage(MessagesUtils.getMessage("CombatLog.combat-join"));
        }
        if (!attacker.isInCombat()) {
            attacker.getPlayer().sendMessage(MessagesUtils.getMessage("CombatLog.combat-join"));
        }
        PluginManager.getInstance().getCombatManager().getManager().put(attacker.getPlayer(), duration);
        PluginManager.getInstance().getCombatManager().getManager().put(player.getPlayer(), duration);
  }
}