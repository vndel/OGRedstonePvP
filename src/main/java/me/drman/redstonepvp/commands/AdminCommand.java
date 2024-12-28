package me.drman.redstonepvp.commands;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.AnvilType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashSet;

@Command("admin")
@CommandPermission("redstonepvp.admin")
public class AdminCommand {
    @Subcommand("stats")
    public void onStats(String mode,Player target,String type,int value){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(target);
        if (mode.equalsIgnoreCase("set")){
            if (type.equalsIgnoreCase("kills")){
                redPlayer.setKills(Integer.valueOf(value));
            }else if (type.equalsIgnoreCase("deaths")){
                redPlayer.setDeaths(Integer.valueOf(value));
            }else if (type.equalsIgnoreCase("streak")) {
                redPlayer.setStreak(Integer.valueOf(value));
            }if (type.equalsIgnoreCase("maxstreak")) {
                redPlayer.setMaxStreak(Integer.valueOf(value));
            }if (type.equalsIgnoreCase("playtime")) {
                target.setStatistic(Statistic.PLAY_ONE_TICK, value*20);
            }
        }
        if (mode.equalsIgnoreCase("add")){
            if (type.equalsIgnoreCase("kills")){
                redPlayer.addKills(Integer.valueOf(value));
            }else if (type.equalsIgnoreCase("deaths")){
                redPlayer.addDeaths(Integer.valueOf(value));
            }else if (type.equalsIgnoreCase("streak")) {
                redPlayer.addStreak(Integer.valueOf(value));
            }if (type.equalsIgnoreCase("maxstreak")) {
                redPlayer.addMaxStreak(Integer.valueOf(value));
            }if (type.equalsIgnoreCase("votes")) {
                target.setStatistic(Statistic.PLAY_ONE_TICK, target.getStatistic(Statistic.PLAY_ONE_TICK)+(value)*20);
            }
        }
    }
    @Subcommand("axes")
    public void onAxe(String mode,Player target,String type) {
        if (mode.equalsIgnoreCase("give")){
            try{
                target.getInventory().addItem(Axes.getPlugin().getAxesManager().getByName(type).getItem());
            }catch (Exception ignored){}
        }
    }
    @Subcommand("anvils")
    public void onAnvils(Player player, String mode, AnvilType anvilType) {
        if (mode.equalsIgnoreCase("set")){
            if (anvilType.equals(AnvilType.REPAIR)){
                PluginManager.getInstance().getAnvilsManager().getRepairAnvils().add(player.getTargetBlock((HashSet<Byte>) null,4).getLocation());
                player.sendMessage(MessagesUtils.format("&aRepair anvil set."));
            }else{
                player.sendMessage(MessagesUtils.format("&aMerge anvil set."));
                PluginManager.getInstance().getAnvilsManager().getMergeAnvils().add(player.getTargetBlock((HashSet<Byte>) null,4).getLocation());
            }
        }
    }
    @DefaultFor("admin")
    public void onHelp(Player player){
        player.sendMessage(ChatColor.RED+"Usage:");
        player.sendMessage(ChatColor.RED+"- admin stats set <player> <type> <value>");
        player.sendMessage(ChatColor.RED+"- admin axes give <player> <AxeName>");
        player.sendMessage(ChatColor.RED+"- admin anvils set <type>");

    }

}
