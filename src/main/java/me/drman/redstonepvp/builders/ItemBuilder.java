package me.drman.redstonepvp.builders;

import com.google.common.collect.Lists;
import me.clip.placeholderapi.PlaceholderAPI;
import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.shop.ShopItem;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder
{
    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack.clone();
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setGlowing(boolean b) {
        ItemMeta meta = this.itemStack.getItemMeta();
        if(b){
            enchantment(Enchantment.LUCK);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        this.itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder infinite() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder infinite(boolean b) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.spigot().setUnbreakable(b);
        this.itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder lore(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.add(MessagesUtils.format(name));
        meta.setLore(lore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String... lores) {
        List<String> list = Lists.newArrayList();
        ItemMeta meta = this.itemStack.getItemMeta();
        for (String lore : lores) {
            list.add(ChatColor.translateAlternateColorCodes('&', lore));
        }
        meta.setLore(list);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(List<String> lores) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> finalLore = new ArrayList<>();
        for (String lore : lores) {
            finalLore.add(MessagesUtils.format(lore));
        }
        meta.setLore(finalLore);
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder durability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder data(int data) {
        return durability((short) data);
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        this.itemStack.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder skullOwner(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        SkullMeta skull = (SkullMeta) meta;
        skull.setOwner(name);
        this.itemStack.setItemMeta(skull);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(new ArrayList<String>());
        this.itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (Enchantment e : this.itemStack.getEnchantments().keySet()) {
            this.itemStack.removeEnchantment(e);
        }
        return this;
    }

    public ItemBuilder color(Color color) {
        if (this.itemStack.getType() == Material.LEATHER_BOOTS || this.itemStack.getType() == Material.LEATHER_CHESTPLATE || this.itemStack.getType() == Material.LEATHER_HELMET || this.itemStack.getType() == Material.LEATHER_LEGGINGS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.itemStack.getItemMeta();
            meta.setColor(color);
            this.itemStack.setItemMeta(meta);
            return this;
        }
        throw new IllegalArgumentException("color() only applicable for leather armor!");
    }
    public ItemBuilder flag(ItemFlag flag){
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(flag);
        itemStack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder applyPlaceholders(Player player) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> finalLore = new ArrayList<>();
        for (String lore : meta.getLore()) {
            finalLore.add(PlaceholderAPI.setPlaceholders(player,lore));
        }
        meta.setLore(finalLore);
        this.itemStack.setItemMeta(meta);
        return this;
    }


    public ItemStack build(){
        return itemStack;
    }

    public ItemBuilder applyShopPlaceholders(ShopItem item,Player p) {
        ItemMeta meta = this.itemStack.getItemMeta();
        List<String> finalLore = new ArrayList<>();
        RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(p);
        for (String lore : meta.getLore()) {
            finalLore.add(lore.replace("%redstonepvp_item_price%",""+(int) ((100 - redPlayer.getDiscount()) * item.getVaultPrice()) /100));
        }
        meta.setLore(finalLore);
        this.itemStack.setItemMeta(meta);
        return this;
    }
}