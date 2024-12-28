package me.drman.redstonepvp.commands;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FixAllCommand {
    private final HashMap<Player, Long> cooldowns = new HashMap<>();

    @Command("fixall")
    @CommandPermission("redstonepvp.command.fixall")
    public void fixAll(BukkitCommandActor actor) {
        Player player = actor.getAsPlayer();
        if (RedPvP.getPlugin().getPlayerManager().get(player).inDuel()) return;
        long now = System.currentTimeMillis();
        if (cooldowns.containsKey(player)) {
            long cooldownEnd = cooldowns.get(player);
            if (now < cooldownEnd) {
                long timeLeft = (cooldownEnd - now) / 1000;
                player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"You must wait " + MessagesUtils.timeFormat((int) timeLeft) + " seconds before using this command again."));
                return;
            }
        }
        ItemStack[] contents = player.getInventory().getContents().clone();
        for(ItemStack itemStack : contents){
            if (!(itemStack==null||itemStack.getType().equals(Material.AIR))) {
                if (!Axes.getPlugin().getAxesManager().isAxe(itemStack)) {
                    itemStack.setDurability((short) 0);
                }
            }
        }
        ItemStack[] armorContents = player.getInventory().getArmorContents().clone();
        for(ItemStack itemStack : armorContents){
            if (!(itemStack==null||itemStack.getType().equals(Material.AIR))) {
                if (!Axes.getPlugin().getAxesManager().isAxe(itemStack)) {
                    itemStack.setDurability((short) 0);
                }
            }
        }
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"Your item(s) has been repaired!"));

        cooldowns.put(player, now + TimeUnit.HOURS.toMillis(1));
    }
}
