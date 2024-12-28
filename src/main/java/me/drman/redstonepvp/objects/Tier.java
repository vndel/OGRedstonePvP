package me.drman.redstonepvp.objects;

import lombok.Getter;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tier {

    @Getter private final String name;

    @Getter private final int slot;
    @Getter private final int booster;
    @Getter private final String displayName;
    @Getter private final String tierName;
    @Getter private final int requiredVotes;
    @Getter private final int requiredHours;

    @Getter private final ItemStack displayItem;

    public Tier(String name, int slot, int booster, String displayName, String tierName, int requiredVotes, int requiredHours, ItemStack displayItem) {
        this.name = name;
        this.slot = slot;
        this.booster = booster;
        this.displayName = MessagesUtils.format(displayName);
        this.tierName = MessagesUtils.format(tierName);
        this.requiredVotes = requiredVotes;
        this.requiredHours = requiredHours;
        this.displayItem = new ItemBuilder(displayItem).name(displayName).build();
    }

    public boolean isEligible(Player player) {
        return (RedPvP.getPlugin().getPluginManager().getTierManager().getTier(player).getRequiredHours() >= requiredHours)&&(RedPvP.getPlugin().getPluginManager().getTierManager().getTier(player).getRequiredVotes() >= requiredVotes);
    }

    @Override
    public String toString() {
        return "Tier{" +
                "name='" + name + '\'' +
                ", slot=" + slot +
                ", booster=" + booster +
                ", displayName='" + displayName + '\'' +
                ", tierName='" + tierName + '\'' +
                ", requiredVotes=" + requiredVotes +
                ", requiredHours=" + requiredHours +
                ", displayItem=" + displayItem +
                '}';
    }
}