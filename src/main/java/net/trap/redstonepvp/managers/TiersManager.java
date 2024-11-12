package me.drman.redstonepvp.managers;


import lombok.Getter;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.objects.Tier;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class TiersManager {

    @Getter private ArrayList<Tier> manager = new ArrayList<>();

    public void register(Tier Tier) {
        manager.add(Tier);
    }

    public void loadAll(){
        ConfigurationSection section = ConfigWrapper.valueOf(ConfigurationType.TIERS).getConfiguration().getConfigurationSection("Tiers");
        FileConfiguration cfg = ConfigWrapper.valueOf(ConfigurationType.TIERS).getConfiguration();
        for (String s : section.getKeys(false)){
            register(new Tier(s,
                    cfg.getInt("Tiers."+s+".slot"),
                    cfg.getInt("Tiers."+s+".booster"),
                    cfg.getString("Tiers."+s+".display-name"),
                    cfg.getString("Tiers."+s+".tier-name"),
                    cfg.getInt("Tiers."+s+".requiredVotes"),
                    cfg.getInt("Tiers."+s+".requiredHours"),
                    new ItemBuilder(ItemsUtils.getSkull(cfg.getString("Tiers."+s+".head"))).lore(cfg.getStringList("Tiers."+s+".lore")).build()
            ));
        }
    }


    public Tier getTier(String name) {
        return manager.stream().filter(Tier -> Tier.getName().equals(name)).findFirst().orElse(null);
    }

    public Tier getTier(Player player) {
        int votes = (int) RedPvP.getPlugin().getPlayerManager().get(player).getVotes();
        int hours = (int) RedPvP.getPlugin().getPlayerManager().get(player).getPlayTime();
        return getTier(votes,hours);
    }
    public Tier getTier(int votes,int hours){
        Tier p = new Tier("error",1,1,"error","error",0,0,new ItemStack(Material.BARRIER));
        for (Tier Tier : PluginManager.getInstance().getTierManager().getManager()){
            if (Tier.getRequiredHours() <= hours && Tier.getRequiredVotes() <= votes&& Tier.getRequiredVotes()>=p.getRequiredVotes()&&Tier.getRequiredHours()>=p.getRequiredHours()) {
                p = Tier;
            }
        }
        return p;
    }
}