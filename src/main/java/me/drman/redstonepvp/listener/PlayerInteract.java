package me.drman.redstonepvp.listener;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.AnvilType;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.managers.RandomBoxManager;
import me.drman.redstonepvp.menus.ConfirmPayment;
import me.drman.redstonepvp.objects.RandomBox;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.tasks.DropPartyTask;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerInteract implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        Entity entity = e.getRightClicked();
        if (!(entity instanceof ItemFrame))
            return;
        ItemFrame itemframe = (ItemFrame) e.getRightClicked();
        ItemStack air = new ItemStack(Material.AIR);
        if (itemframe.getItem().isSimilar(air))
            return;
        e.setCancelled(true);
        ItemStack item = itemframe.getItem();
        PlayerInventory playerInventory = p.getInventory();
        if (item.getType().equals(Material.GOLDEN_APPLE) || item
                .getType().equals(Material.ARROW)) {
            item.setAmount(64);
            playerInventory.addItem(new ItemStack[]{item});
        } else {
            playerInventory.addItem(new ItemStack[]{item});
        }
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 5.0F, 1.0F);
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            for (RandomBox box : PluginManager.getInstance().getRandomBoxManager().getRandomBoxes()) {
                if (box.getBlockLocation().equals(e.getClickedBlock().getLocation())) {
                    if (e.getPlayer().getInventory().contains(RandomBox.getPrice().getType(),RandomBox.getPrice().getAmount())) {
                        if (box.isActive()) {
                            e.getPlayer().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + MessagesUtils.getMessage("RandomBox.busy-box")));
                            return;
                        }
                        e.getPlayer().getInventory().removeItem(new ItemStack[]{ new ItemStack(RandomBox.getPrice().getType(),RandomBox.getPrice().getAmount())});
                        box.activate(e.getPlayer());
                    } else {
                        e.getPlayer().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + MessagesUtils.getMessage("RandomBox.poor-player")));
                    }
                }
            }
            if (e.getClickedBlock().getLocation().equals(PluginManager.getInstance().getChestManager().getChest())){
                e.setCancelled(true);
                RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer());
                if (e.getPlayer().hasPermission("redstonepvp.chest.deny")) {
                    redPlayer.sendMessage("&cYou can open the chest after: &r"+redPlayer.getChestCoolDown());
                    return;
                }
                Collections.shuffle(PluginManager.getInstance().getChestManager().getCommands(), new Random());
                List<String> selectedItems = PluginManager.getInstance().getChestManager().getCommands().subList(0, ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("parkour-chest.items-to-recieve"));
                for (String item : selectedItems) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),item.replace("%player%",redPlayer.getPlayer().getName()));
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+e.getPlayer().getName()+" permission settemp redstonepvp.chest.deny true 1d");
                redPlayer.sendMessage("&aYou claimed your parkour price, try again tomorrow");
                e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.LEVEL_UP,5,5);
            }
            if (e.getClickedBlock().getLocation().equals(PluginManager.getInstance().getDropParty().getLocation())){
                e.setCancelled(true);
                RedPlayer redPlayer = RedPvP.getPlugin().getPlayerManager().get(e.getPlayer());
                e.getPlayer().sendMessage("");
                redPlayer.sendMessage("&fDrop party will start in "+MessagesUtils.timeFormat(DropPartyTask.getTimer())+"&f.");
                redPlayer.sendMessage("&fDrop party requires at least &e"+ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getInt("DropParty.players-required")+" players &fonline");
                e.getPlayer().sendMessage("");
            }
            if (PluginManager.getInstance().getAnvilsManager().isRepairAnvil(e.getClickedBlock().getLocation())){
                e.setCancelled(true);
                Player player = (Player) e.getPlayer();
                if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType().equals(Material.AIR)||player.getItemInHand().getDurability() == 0){
                    player.sendMessage(MessagesUtils.format("&b&l[REPAIR] &fThis item is not repairable!"));
                    player.closeInventory();
                    return;
                }
                if (Axes.getPlugin().getAxesManager().isAxe(player.getItemInHand())){
                    player.sendMessage(MessagesUtils.format("&b&l[REPAIR] &fAxes are not repairable!"));
                    player.closeInventory();
                    return;
                }
                String n = player.getItemInHand().getType().name().toLowerCase();
                if (!n.contains("diamond")){
                    if (!n.contains("bow")) {
                        player.sendMessage(MessagesUtils.format("&b&l[REPAIR] &fThis item is not repairable!"));
                        return;
                    }
                }
                new ConfirmPayment(e.getPlayer(), AnvilType.REPAIR).open(e.getPlayer());
            }
            if (PluginManager.getInstance().getAnvilsManager().isMergeAnvil(e.getClickedBlock().getLocation())){
                e.setCancelled(true);
                new ConfirmPayment(e.getPlayer(), AnvilType.MERGE).open(e.getPlayer());
            }
        }
    }
    @EventHandler
    public void onBreak(HangingBreakByEntityEvent e) {
        Hanging hanging = e.getEntity();
        Entity en = e.getRemover();
        if (!(hanging instanceof ItemFrame))
            return;
        if (!(en instanceof Player))
            return;
        Player p = (Player) e.getRemover();
        if (!p.hasPermission("*"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ItemFrame)
            e.setCancelled(true);
    }
}
