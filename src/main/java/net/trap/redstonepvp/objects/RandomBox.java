package me.drman.redstonepvp.objects;

import com.google.gson.annotations.Expose;
import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.effect.BleedEffect;
import lombok.Getter;
import lombok.Setter;
import me.drman.RedPvP;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.managers.RandomBoxManager;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBox {
    @Expose
    @Getter
    private int ID;
    private String location;
    @Expose
    @Getter
    private boolean isActive;
    private Item displayedItem;
    private transient int i;
    private transient int taskID;

    @Getter
    @Setter
    private static ItemStack price = new ItemStack(Material.GOLD_INGOT, 10); // 10 gold by default

    public RandomBox(int ID, String loc) {
        this.ID = ID;
        this.location = loc;
    }

    public void activate(Player p) {
        if (RedPvP.getPlugin().getPlayerManager().get(p).isFull()) {
            p.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + MessagesUtils.getMessage("RandomBox.inventory-full")));
            return;
        }

        isActive = true;
        EffectManager em = new EffectManager(RedPvP.getPlugin());
        Effect effect = new BleedEffect(em);
        effect.setLocation(getDisplayLocation());
        effect.start();
        p.playSound(getDisplayLocation(), Sound.ENDERMAN_TELEPORT, 5, 0);
        displayItem(PluginManager.getInstance().getRandomBoxManager().getItems().get(new Random().nextInt(PluginManager.getInstance().getRandomBoxManager().getItems().size())));
        i = 4 + (int) (Math.random() * ((8 - 4) + 1));
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(RedPvP.getPlugin(), () -> {
            if (i > 0) {
                changeItem(PluginManager.getInstance().getRandomBoxManager().getItems().get(new Random().nextInt(PluginManager.getInstance().getRandomBoxManager().getItems().size())));
                i--;
            }
            if (i == 0) {
                end(p);
            }
        }, 0, 20);
    }

    public void end(Player player) {
        if (displayedItem != null) {
            displayedItem.remove();
        }
        isActive = false;
        Bukkit.getScheduler().cancelTask(taskID);
        player.getInventory().addItem(displayedItem.getItemStack());
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + MessagesUtils.getMessage("RandomBox.done").replace("%item%", displayedItem.getItemStack().getItemMeta().getDisplayName())));
    }

    public Location getBlockLocation() {
        return LocationsUtils.deParseLocation(location);
    }

    public Location getDisplayLocation() {
        return LocationsUtils.LocationCentralize(new Location(getBlockLocation().getWorld(), getBlockLocation().getX(), getBlockLocation().getY() + 1, getBlockLocation().getZ()));
    }

    private void displayItem(ItemStack item) {
        displayedItem = getDisplayLocation().getWorld().dropItem(getDisplayLocation(), item);
        displayedItem.setPickupDelay(1000);
        displayedItem.setVelocity(new Vector(0, 0, 0));
    }

    private void changeItem(ItemStack item) {
        if (item.equals(displayedItem.getItemStack())) {
            changeItem(PluginManager.getInstance().getRandomBoxManager().getItems().get(new Random().nextInt(PluginManager.getInstance().getRandomBoxManager().getItems().size())));
        } else {
            displayedItem.setItemStack(item);
        }
    }

}
