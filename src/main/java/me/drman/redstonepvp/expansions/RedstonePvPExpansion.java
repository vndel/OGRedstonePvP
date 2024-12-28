package me.drman.redstonepvp.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.drman.RedPvP;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.DropParty;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.tasks.DropPartyTask;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class RedstonePvPExpansion extends PlaceholderExpansion {
    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return "OGRedstonePvP";
    }

    public String getIdentifier() {
        return "RedstonePvP";
    }

    public String getVersion() {
        return "1.0";
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        RedPlayer miner = RedPvP.getPlugin().getPlayerManager().get(player);
        try {
            if (identifier.equalsIgnoreCase("stats_kills"))
                return String.valueOf(miner.getKills());
            if (identifier.equalsIgnoreCase("stats_deaths"))
                return String.valueOf(miner.getDeaths());
            if (identifier.equalsIgnoreCase("stats_streak"))
                return String.valueOf(miner.getStreak());
            if (identifier.equalsIgnoreCase("stats_maxstreak"))
                return String.valueOf(miner.getMaxStreak());
            if (identifier.equalsIgnoreCase("tier_displayname"))
                return String.valueOf(miner.getCurrentTier().getDisplayName());
            if (identifier.equalsIgnoreCase("tier"))
                return String.valueOf(miner.getCurrentTier().getTierName());
            if (identifier.equalsIgnoreCase("stats_ratio")) {
                if (miner.getKD() == -1) {
                    return MessagesUtils.format("&eN/A");
                }
                return String.format("%.2f", miner.getKD());
            }
            if (identifier.equalsIgnoreCase("dropparty"))
                return MessagesUtils.timeFormat(DropPartyTask.getTimer());
            if (identifier.equalsIgnoreCase("discount"))
                return MessagesUtils.format(""+miner.getDiscount());
            if (identifier.equalsIgnoreCase("chest_cooldown"))
                return miner.getChestCoolDown();
             return null;
        }catch (Exception e){
            return MessagesUtils.format("&c&lERROR");
        }
    }
}