package me.drman.redstonepvp.commands;

import me.drman.redstonepvp.menus.TiersMenu;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class TiersCommand {

    @Command({"Tiers","Tier","Rank"})
    public void onTier(Player p){
        new TiersMenu(p).open(p);
    }

}
