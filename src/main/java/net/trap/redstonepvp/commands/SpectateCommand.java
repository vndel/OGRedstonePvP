package me.drman.redstonepvp.commands;

import me.drman.RedPvP;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import revxrsal.commands.annotation.Command;

public class SpectateCommand {

    @Command("spectate")
    public void onSpectate(Player player){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
        if (redPlayer.inDuel()) return;
        if (redPlayer.isSpectating()){
            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(false);
            player.teleport(PluginManager.getInstance().getSpawnLocation());
            redPlayer.setSpectating(false);
            redPlayer.sendMessage("&cSpectating mode disabled!");
        }else{
            player.setGameMode(GameMode.SPECTATOR);
            player.setFlying(true);
            redPlayer.setSpectating(true);
            redPlayer.sendMessage("&aSpectating mode enabled!");
        }
    }

}
