package me.drman.redstonepvp.commands;

import me.drman.RedPvP;

import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
@Command("stats")
public class StatsCommand {

    @DefaultFor("stats")
    public void onStats(CommandSender sender, @Optional Player target){
        RedPlayer redPlayer;
        if (target==null){
            redPlayer = RedPvP.getPlugin().getPlayerManager().get((Player) sender);
        }else{
            redPlayer = RedPvP.getPlugin().getPlayerManager().get((Player) target);
        }
        sender.sendMessage(MessagesUtils.format("&b"+redPlayer.getName()+"'s stats:"));
        sender.sendMessage(MessagesUtils.format(" &8◆ &fKills: &b"+redPlayer.getKills()));
        sender.sendMessage(MessagesUtils.format(" &8◆ &fDeaths: &b"+redPlayer.getDeaths()));
        sender.sendMessage(MessagesUtils.format(" &8◆ &fRatio: &b"+redPlayer.getKD()));
        sender.sendMessage(MessagesUtils.format(" &8◆ &fStreak: &b"+redPlayer.getStreak()));
        sender.sendMessage(MessagesUtils.format(" &8◆ &fMaxStreak: &b"+redPlayer.getMaxStreak()));
    }

}
