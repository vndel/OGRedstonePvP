package me.drman.redstonepvp.objects.shop;

import fr.mrmicky.fastinv.FastInv;
import lombok.Data;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.menus.ShopMenu;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
@Data
public class ShopCategory {

    private String name;
    private String title;
    private int slot;
    private int size;
    private ItemStack displayItem;
    private List<ShopItem> items;

    public ShopCategory(String name,String title,int slot, int size, List<ShopItem> items,ItemStack displayItem) {
        this.name = name;
        this.slot = slot;
        this.title = title;
        this.size = size;
        this.items = items;
        this.displayItem = displayItem;
    }
    public void open(Player p){
        FastInv inv = new FastInv(size,title);
        inv.setItem(40,new ItemBuilder(Material.ARROW).name("&cBack to main menu").build(), event -> {
            event.getWhoClicked().closeInventory();
            new ShopMenu(p).open((Player) event.getWhoClicked());
        });
        for (ShopItem item : items){
            inv.setItem(item.getSlot(),new ItemBuilder(item.getItem()).applyPlaceholders(p.getPlayer()).applyShopPlaceholders(item,p).build(),event -> {
                boolean acceptsCoins = item.acceptsCoins();
                boolean acceptsMaterials = item.acceptsMaterials();
                RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(p);
                boolean hasCoins = RedPvP.getPlugin().getEconomy().has(p.getPlayer(), (int) ((100 - redPlayer.getDiscount()) * item.getVaultPrice()) /100);
                boolean hasMaterials = p.getInventory().contains(item.getMatPrice().getType(), item.getMatPrice().getAmount());
                if (p.getInventory().firstEmpty() == -1){
                    p.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cPurchase failed! Please empty your inventory."));
                    return;
                }
                if (acceptsCoins&&acceptsMaterials&&hasCoins&&hasMaterials){
                    FastInv confirmMenu = new FastInv(9*2,"Payment confirmation");
                    confirmMenu.setItem(13,new ItemBuilder(Material.ARROW).name("&cBack to main menu").build(), event1 -> {
                        inv.open(p);
                    });
                    confirmMenu.setItem(2,new ItemBuilder(Material.DOUBLE_PLANT).name("&6Pay with coins!").build(), event1 -> {
                        coinsBuy(p,item);
                    });
                    confirmMenu.setItem(6,new ItemBuilder(Material.GOLD_INGOT).name("&6Pay with materials!").build(), event1 -> {
                        materialsBuy(p,item);
                    });
                    confirmMenu.open(p);
                }else if (!acceptsCoins||!hasCoins) {
                    materialsBuy(p,item);
                } else if (!acceptsMaterials||!hasMaterials) {
                    coinsBuy(p, item);
                }
            });
        }
        inv.open(p);
    }

    public void coinsBuy(Player p,ShopItem item){
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(p);
        int newPrice = ((100-redPlayer.getDiscount())*item.getVaultPrice())/100;
        if (RedPvP.getPlugin().getEconomy().has(p,newPrice)) {
            RedPvP.getPlugin().getEconomy().withdrawPlayer(p, newPrice);
            p.getInventory().addItem(new ItemBuilder(item.getNormalVersion()).amount(item.getItem().getAmount()).build());
        }else{
            redPlayer.sendMessage("Insufficient balance you can't buy this item");
        }
    }
    public void materialsBuy(Player p,ShopItem item) {
        if (p.getInventory().contains(item.getMatPrice().getType(), item.getMatPrice().getAmount())){
            removeItem(item.getMatPrice().clone(), p.getInventory());
            p.getInventory().addItem(new ItemBuilder(item.getNormalVersion()).amount(item.getItem().getAmount()).build());
        }else{
            p.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"Insufficient balance you can't buy this item"));
        }
    }
    public void removeItem(ItemStack i, Inventory inv) {
        int itemIdToRemove = i.getType().getId();
        int amountToRemove = i.getAmount();

        for (int slot = 0; slot < inv.getSize(); slot++) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType().getId() == itemIdToRemove) {
                int amountToTransfer = Math.min(amountToRemove, item.getAmount());
                item.setAmount(item.getAmount() - amountToTransfer);
                amountToRemove -= amountToTransfer;
                if (item.getAmount() == 0) {
                    inv.setItem(slot, null);
                }
                if (amountToRemove == 0) {
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ShopCategory{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", slot=" + slot +
                ", size=" + size +
                ", displayItem=" + displayItem +
                ", items=" + items +
                '}';
    }
}