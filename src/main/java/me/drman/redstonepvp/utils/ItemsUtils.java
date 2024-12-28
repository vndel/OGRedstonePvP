package me.drman.redstonepvp.utils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.drman.Axes;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.managers.PluginManager;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ItemsUtils {

    public static ItemStack getItem(String path, FileConfiguration yaml){
        try {
            ItemBuilder itemBuilder = new ItemBuilder(Material.BARRIER).name("&cERROR - Report to admin");
            if (yaml.contains(path+".type")) {
                if (yaml.get(path + ".type") instanceof Integer) {
                    itemBuilder = new ItemBuilder(Material.getMaterial(yaml.getInt(path + ".type")));
                }else{
                    itemBuilder = new ItemBuilder(Material.getMaterial(yaml.getString(path + ".type").toUpperCase()));
                }
            } else if (yaml.contains(path+"material")) {
                if (yaml.contains(path+".material")) {
                    if (yaml.get(path + ".material") instanceof Integer) {
                        itemBuilder = new ItemBuilder(Material.getMaterial(yaml.getInt(path + ".material")));
                    } else {
                        itemBuilder = new ItemBuilder(Material.getMaterial(yaml.getString(path + ".material").toUpperCase()));
                    }
                }
            } else if(yaml.contains(path+".head")){
                itemBuilder = new ItemBuilder(getSkull(yaml.getString(path + ".head")));
            }else if (yaml.contains(path+".axe")){
                itemBuilder = new ItemBuilder(Axes.getPlugin().getAxesManager().getByName(yaml.getString(path+".axe")).getItem());
            }
            if (itemBuilder.build().getType().equals(Material.AIR)) return itemBuilder.build();
            if (yaml.contains(path + ".amount"))
                itemBuilder.amount(yaml.getInt(path + ".amount"));
            if (yaml.contains(path + ".display-name"))
                itemBuilder.name(MessagesUtils.format(yaml.getString(path + ".display-name")));
            if (yaml.contains(path+".data"))
                itemBuilder.data((short) yaml.getInt(path + ".data"));
            itemBuilder.lore(yaml.getStringList(path + ".lore"));
            if (yaml.contains(path + ".enchantments")) {
                for (String s : yaml.getStringList(path + ".enchantments")) {
                    itemBuilder.enchantment(Enchantment.getByName(s.split(":")[0].toUpperCase()), Integer.parseInt(s.split(":")[1]));
                }
            }
            if (yaml.contains(path + ".glowing")) {
                itemBuilder.setGlowing(yaml.getBoolean(path + ".glowing"));
            }
            itemBuilder.infinite(yaml.getBoolean(path + ".unbreakable"));

            // Apply item flags
            if (yaml.contains(path + ".flags")) {
                for (String flag : yaml.getStringList(path + ".flags")) {
                    itemBuilder.flag(ItemFlag.valueOf(flag.toUpperCase()));
                }
            }

            return itemBuilder.build();
        } catch (Exception e){
            System.out.println("Failed to load the item in '"+path+"'");
            e.printStackTrace();
            return null;
        }
    }
    public static ItemStack getNormalVersion(ItemStack item){
        fr.mrmicky.fastinv.ItemBuilder i = new fr.mrmicky.fastinv.ItemBuilder(item.clone());
        i.name(null);
        i.lore((List<String>)null);
        i.removeFlags();
        return i.build();
    }
    public static ItemStack simpleItem(String s){
        if (s==null){
            return null;
        }
        s = s.toUpperCase();
        return new ItemStack(Material.getMaterial(s.split(":")[0]),Integer.parseInt(s.split(":")[1]));
    }

    public static ItemStack getSkull(String value) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        UUID uuid = UUID.randomUUID();
        GameProfile profile1 = new GameProfile(uuid, null);
        profile1.getProperties().put("textures", new Property("textures", value));
        setProfile(skull, profile1);
        return skull;
    }
    private static void setProfile(ItemStack item, GameProfile profile) {
        ItemMeta meta = item.getItemMeta();
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        item.setItemMeta(meta);
    }
    public static void setItem(ItemStack item, String path, FileConfiguration yaml) {
        yaml.set(path + ".type", item.getType().toString());
        yaml.set(path + ".amount", item.getAmount());
        if ((Byte)item.getData().getData() != null){
            yaml.set(path + ".data", item.getData().getData());
        }
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();

            if (meta.hasDisplayName()) {
                yaml.set(path + ".display-name", meta.getDisplayName());
            }

            if (meta.hasLore()) {
                yaml.set(path + ".lore", meta.getLore());
            }

            if (meta.hasEnchants()) {
                List<String> enchantments = new ArrayList<>();
                for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
                    enchantments.add(entry.getKey().getName().toUpperCase() + ":" + entry.getValue());
                }
                yaml.set(path + ".enchantments", enchantments);
            }

            // Save item flags
            if (!meta.getItemFlags().isEmpty()) {
                List<String> flags = new ArrayList<>();
                for (ItemFlag flag : meta.getItemFlags()) {
                    flags.add(flag.name());
                }
                yaml.set(path + ".flags", flags);
            }
        }

    }

    public static int getFortuneLevel(ItemStack item) {
        int fortuneLevel = 0;
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        if (enchantments.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortuneLevel = enchantments.get(Enchantment.LOOT_BONUS_BLOCKS);
        }
        return fortuneLevel;
    }

}