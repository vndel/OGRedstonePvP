package me.drman.redstonepvp.tasks;

import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TierTrackingTask implements Runnable{
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()){
            RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
            if (redPlayer.getCurrentTier()==null) return;
            if (!redPlayer.getCurrentTier().equals(redPlayer.getCurrentTier())){
                redPlayer.setCurrentTier(redPlayer.getCurrentTier());
                redPlayer.sendMessage("&fYour tier has been upgraded to "+redPlayer.getCurrentTier().getDisplayName());
                redPlayer.sendMessage("&d"+redPlayer.getPlayer().getName()+"&f's tier has been upgraded to 7aTier "+redPlayer.getCurrentTier().getDisplayName()+"&f!");
            }
        }
    }
}
