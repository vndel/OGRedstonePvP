package me.drman.redstonepvp.tasks;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CombatLogTask implements Runnable {
    private int duration = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("combat-log.duration");
    @Override
    public void run() {
        List<Player> remove = new ArrayList<>();
        for (Map.Entry<Player, Integer> entry : PluginManager.getInstance().getCombatManager().getManager().entrySet()) {
            RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(entry.getKey());
            if (entry.getValue() <= 0) {
                entry.getKey().sendMessage(MessagesUtils.getMessage("CombatLog.combat-quit"));
                remove.add(entry.getKey());
            } else {
                int percent = (entry.getValue()*100)/duration;
                redPlayer.sendAction(MessagesUtils.createProgressBar(percent,15,"●"));
                entry.setValue(entry.getValue() - 1);
            }
        }
        for (Player p : remove) {
            PluginManager.getInstance().getCombatManager().getManager().remove(p);
        }
        remove.clear();
    }
}