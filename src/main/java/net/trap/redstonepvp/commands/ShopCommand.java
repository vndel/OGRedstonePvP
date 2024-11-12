package me.drman.redstonepvp.commands;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.menus.ShopMenu;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;

public class ShopCommand {

    @Command("shop")
    public void onShop(Player player){
        new ShopMenu(player).open(player);
    }

}
