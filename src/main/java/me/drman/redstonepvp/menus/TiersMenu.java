package me.drman.redstonepvp.menus;

import fr.mrmicky.fastinv.FastInv;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.Tier;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TiersMenu extends FastInv {

    public TiersMenu(Player player) {
        super(45,MessagesUtils.format("Tiers List"));
        for (int i=44;i>-1;i--){
            setItem(i,new ItemStack(Material.STAINED_GLASS_PANE,1,(byte)8));
        }
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(player);
        setItem(40, new ItemBuilder(Material.PAPER).amount(1).name("&fYour Tier: "+redPlayer.getCurrentTier().getDisplayName()).lore("","&fYour Votes: &d"+redPlayer.getVotes(),"&fYour Hours: &d"+redPlayer.getPlayTime()).build());
        for (Tier Tier : RedPvP.getPlugin().getPluginManager().getTierManager().getManager()){
            setItem(Tier.getSlot(),Tier.getDisplayItem());
        }
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
