package me.drman.redstonepvp.managers;

import com.google.common.collect.Maps;
import lombok.Getter;
import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.objects.shop.ShopCategory;
import me.drman.redstonepvp.objects.shop.ShopItem;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

@Getter
public class ShopManager {
    private final List<ShopCategory> categories = new ArrayList<>();
    private final TreeMap<Integer,String> discounts = new TreeMap<>();

    public void loadAll() {
        File directory = new File(RedPvP.getPlugin().getDataFolder() + "/Categories");
        directory.mkdir(); // Create directory if it doesn't exist

        // Get the "shop-categories" section from shop.yml
        ConfigurationSection shopCategoriesSection = ConfigWrapper.valueOf(ConfigurationType.SHOP).getConfiguration().getConfigurationSection("shop-categories");

        if (shopCategoriesSection != null) {
            for (String categoryName : shopCategoriesSection.getKeys(false)) {
                MessagesUtils.send("&eloading &c" + categoryName + " &ecategory");

                // Use getConfigurationSection instead of getString for category config
                FileConfiguration categoryConfig = YamlConfiguration.loadConfiguration(new File(directory + "/" + categoryName + ".yml"));
                ShopCategory shopCategory = new ShopCategory(
                        categoryName,
                        categoryConfig.getString("category-settings.title"),
                        ConfigWrapper.valueOf(ConfigurationType.SHOP).getInt("shop-categories."+categoryName + ".slot"), // Access slot from "shop-categories"
                        categoryConfig.getInt("category-settings.size"),
                        new ArrayList<>(),
                        ItemsUtils.getItem("shop-categories."+categoryName+".display-item",ConfigWrapper.valueOf(ConfigurationType.SHOP).getConfiguration())
                );
                for (String itemName : categoryConfig.getConfigurationSection("category-items").getKeys(false)) {
                    String materialsPrice = null;
                    if (categoryConfig.contains("category-items." + itemName + ".price.materials")) {
                        materialsPrice = categoryConfig.getString("category-items." + itemName + ".price.materials");
                    }
                    int vaultPrice = -1;
                    if (categoryConfig.contains("category-items." + itemName + ".price.vault")) {
                        vaultPrice = categoryConfig.getInt("category-items." + itemName + ".price.vault");
                    }
                    boolean isAxe = categoryConfig.contains("category-items."+itemName+".axe");
                    ItemStack item = ItemsUtils.getItem("category-items." +itemName, categoryConfig);
                    ItemStack normalVersion;
                    if (isAxe){
                        normalVersion = new ItemBuilder(Axes.getPlugin().getAxesManager().getByName(categoryConfig.getString("category-items." +itemName+".axe")).getItem().clone()).build();
                    }else{
                        assert item != null;
                        normalVersion = ItemsUtils.getNormalVersion(item);
                    }
                    ShopItem shopItem = new ShopItem(
                            item,
                            vaultPrice,
                            ItemsUtils.simpleItem(materialsPrice),
                            categoryConfig.getInt("category-items." + itemName + ".slot"),
                            normalVersion
                    );
                    shopCategory.getItems().add(shopItem);
                }
                categories.add(shopCategory); // Add the created ShopCategory
                PluginManager.getInstance().getShopManager().register(shopCategory); // Assuming register is implemented elsewhere
            }
        } else {
            MessagesUtils.send("&cNo shop categories found in shop.yml!");
        }
    }

    public void loadDiscounts(){
        for (String s : ConfigWrapper.valueOf(ConfigurationType.SHOP).getConfiguration().getConfigurationSection("shop-discounts").getKeys(false)){
            discounts.put(ConfigWrapper.valueOf(ConfigurationType.SHOP).getInt("shop-discounts."+s+".discount-percentage"),ConfigWrapper.valueOf(ConfigurationType.SHOP).getString("shop-discounts."+s+".permission"));
        }
    }

    public ShopCategory register(ShopCategory shopCategory) {
        categories.add(shopCategory);
        return shopCategory;
    }
}
