package me.drman.redstonepvp.objects.shop;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.ItemBuilder;
import lombok.Data;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Data
public class ShopItem {

    private ItemStack item;
    private int vaultPrice;
    private ItemStack matPrice;
    private ItemStack normalVersion;
    private int slot;

    public ShopItem(ItemStack item, int vaultPrice, ItemStack matPrice, int slot,ItemStack normalVersion) {
        this.item = item;
        this.vaultPrice = vaultPrice;
        this.matPrice = matPrice;
        this.slot = slot;
        this.normalVersion = normalVersion;
    }
    public boolean acceptsCoins() {
        return vaultPrice != -1;
    }

    public boolean acceptsMaterials() {
        return matPrice != null;
    }
}
