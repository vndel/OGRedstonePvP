package me.drman.redstonepvp.objects;

import lombok.Data;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.DropPartyManager;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.tasks.DropPartyTask;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class DropParty {

    private Location location;
    private int task;
    private final int itemsDropped = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("DropParty.items-dropped");

    public DropParty(Location location) {
        this.location = location;
    }

    public void run(){
        Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&aDropParty has started"));

        Location water = new Location(location.getWorld(),location.getX(),location.getY()+1,location.getZ());
        water.getWorld().getBlockAt(water).setType(Material.WATER);
        AtomicInteger timer = new AtomicInteger(itemsDropped);
        task = Bukkit.getScheduler().scheduleSyncRepeatingTask(RedPvP.getPlugin(),() -> {
            timer.getAndDecrement();
            if (timer.get()>=0) {
                water.getWorld().dropItem(water, getRandomItem());
            }else{
                timer.set(itemsDropped);
                water.getWorld().getBlockAt(water).setType(Material.AIR);
                Bukkit.broadcastMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&fDropParty has finised! The next drop is in &c30 minutes&f."));
                Bukkit.getScheduler().cancelTask(task);
                PluginManager.getInstance().getDropPartyManager().setTaskId(Bukkit.getScheduler().scheduleSyncRepeatingTask(RedPvP.getPlugin(),new DropPartyTask(),0,20));
            }
        },0,20);


    }

    public ItemStack getRandomItem(){
        DropPartyManager dm = PluginManager.getInstance().getDropPartyManager();
        int random = new Random().nextInt(dm.getItems().size());
        return dm.getItems().get(random);
    }
}
