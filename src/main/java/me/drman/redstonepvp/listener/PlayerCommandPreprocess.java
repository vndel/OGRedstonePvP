package me.drman.redstonepvp.listener;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PlayerCommandPreprocess implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
        if (redPlayer.isInCombat()) {
            List<String> blockedCommands = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getStringList("combat-log.blocked-commands");
            List<String> allowedCommands = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getStringList("combat-log.allowed-commands");
            String command = event.getMessage().toLowerCase().replace("/","");
            if (blockedCommands.contains("*") || blockedCommands.contains(command)) {
                if (!allowedCommands.contains(command)) {
                    event.setCancelled(true);
                    player.sendMessage(MessagesUtils.format(MessagesUtils.getMessage("CombatLog.blocked-command")));
                }
            }
        }
    }


}