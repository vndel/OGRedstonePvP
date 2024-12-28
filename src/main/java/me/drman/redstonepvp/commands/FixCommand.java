package me.drman.redstonepvp.commands;

import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Cooldown;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FixCommand {
    private final HashMap<Player, Long> cooldowns = new HashMap<>();

    @Command("fix")
    @CommandPermission("redstonepvp.command.fix")
    public void fix(BukkitCommandActor actor) {
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

        ItemStack item = player.getItemInHand();
        if (item.getDurability() == -1 || Axes.getPlugin().getAxesManager().isAxe(item)) {
            player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + "This Item is not repairable!"));
            return;
        }
        if (item.getDurability()==0){
            player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"This item is already repaired!"));
        }
        item.setDurability((short) 0);
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"Your item has been repaired!"));
        cooldowns.put(player, now + TimeUnit.MINUTES.toMillis(15));
    }
}
