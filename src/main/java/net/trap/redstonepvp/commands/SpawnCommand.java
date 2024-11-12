package me.drman.redstonepvp.commands;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class SpawnCommand {

    @Command("spawn")
    public void onSpawn(Player player){
        Location spawn = PluginManager.getInstance().getSpawnLocation();
        if (spawn == null) {
            player.sendMessage(MessagesUtils.format("&cSpawn isn't set please report this to a staff member."));
            return;
        }
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
        redPlayer.sendMessage("&fTeleporting in &c5 &fseconds. &cDo not move&f!");
        redPlayer.setTeleporting(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(RedPvP.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (redPlayer.isTeleporting()) {
                    ((Player) player).teleport(spawn);
                    redPlayer.sendAction("");
                    redPlayer.setTeleporting(false);
                }
            }
        },5*20);

    }

}