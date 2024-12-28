package me.drman.redstonepvp.menus;

import fr.mrmicky.fastinv.FastInv;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.shop.ShopCategory;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShopMenu extends FastInv {
    public ShopMenu(Player player) {
        super(ConfigWrapper.valueOf(ConfigurationType.SHOP).getInt("shop-settings.size"), ConfigWrapper.valueOf(ConfigurationType.SHOP).getString("shop-settings.title"));
        for (ShopCategory shopCategory : PluginManager.getInstance().getShopManager().getCategories()){
            setItem(shopCategory.getSlot(),new ItemBuilder(shopCategory.getDisplayItem()).applyPlaceholders(player).build(), event -> {
                shopCategory.open((Player) event.getWhoClicked());
            });
        }
    }
}
