package me.drman.redstonepvp.tasks;

import lombok.Getter;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class DropPartyTask implements Runnable {
    @Getter private static int timer;
    private final int requiredPlayers;
    public DropPartyTask(){
        timer = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("DropParty.timer");
        requiredPlayers = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("DropParty.players-required");
    }
    @Override
    public void run() {
        if (timer<=0){
            if (Bukkit.getOnlinePlayers().size()<requiredPlayers){
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cDrop party cancelled due to lack of players!"));
                timer =  ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("DropParty.timer");
                return;
            }
            if (PluginManager.getInstance().getDropParty()==null){

                Bukkit.getScheduler().cancelTask(PluginManager.getInstance().getDropPartyManager().getTaskId());
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cDropParty has not been set up. Please contact an admin."));
                return;
            }
            PluginManager.getInstance().getDropParty().run();
            Bukkit.getScheduler().cancelTask(PluginManager.getInstance().getDropPartyManager().getTaskId());
        }else{
            timer--;
            if (timer == 25 * 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer/60+" min."));
            } else if (timer == 20 * 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer/60+" min."));
            } else if (timer == 15 * 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer/60+" min."));
            } else if (timer == 10 * 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer/60+" min."));
            } else if (timer == 5 * 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer/60+" min."));
            } else if (timer == 60) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 30) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 15) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 5) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 4) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 3) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 2) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            } else if (timer == 1) {
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDrop party will start in &e"+timer+" seconds."));
            }
        }
    }
}
