package me.drman.redstonepvp.commands;

import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

public class BuildCommand {

    @Command("build")
    @CommandPermission("redstonepvp.build")
    public void onBuild(Player player){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
        redPlayer.setBuilding(!redPlayer.isBuilding());
        if (redPlayer.isBuilding()){
            redPlayer.sendMessage("&fYou have &eenabled &fbuild mode.");
            return;
        }
        redPlayer.sendMessage("&fYou have &cdisabled &fbuild mode.");
    }

}
