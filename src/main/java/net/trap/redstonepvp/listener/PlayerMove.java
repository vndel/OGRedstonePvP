package me.drman.redstonepvp.listener;

import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer());
        if (redPlayer.isTeleporting()){
            redPlayer.setTeleporting(false);
            redPlayer.sendMessage("&fYou moved! Teleportation has been canceled");
        }
    }

}
