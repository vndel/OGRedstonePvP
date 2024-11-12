package me.drman.redstonepvp.commands;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.menus.DropPartyMenu;
import me.drman.redstonepvp.objects.DropParty;
import me.drman.redstonepvp.objects.RandomBox;
import me.drman.redstonepvp.utils.JsonUtils;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.HashSet;

@Command({"Redstonepvp","redpvp"})
public class RedstonePvPCommand{

    @Subcommand("setspawn")
    @CommandPermission("redstonepvp.command.setspawn")
    public void setSpawn(Player sender){
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().set("locations.spawn-point",(LocationsUtils.parseLocation(((Player) sender).getLocation())));
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).save();
        PluginManager.getInstance().setSpawnLocation(sender.getLocation());
        sender.sendMessage(RedPvP.getPlugin().getPrefix()+ MessagesUtils.format("&aSpawn set successfully"));
    }

    @Subcommand("connection")
    @CommandPermission("redstonepvp.command.connection")
    public void onConnection(Player sender){
        sender.sendMessage(MessagesUtils.format("&9Server storage is running on is: "+RedPvP.getPlugin().getDataProvider().getDataStorageType().toString()));
    }
    @Subcommand("randombox")
    @CommandPermission("redstonepvp.command.randombox")
    public void onRandomBox(Player player,String action,int id){
        if (action.equalsIgnoreCase("create")){
            PluginManager.getInstance().register(new RandomBox(id, LocationsUtils.parseLocation(player.getTargetBlock((HashSet<Byte>) null,4).getLocation())));
            player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&aYou created a new RandomBox: "+id));
        }
        if (action.equalsIgnoreCase("delete")){
            RandomBox randomBox = PluginManager.getInstance().getRandomBoxManager().getRandomBoxByID(id).get();
            PluginManager.getInstance().getRandomBoxManager().destroyBox(randomBox);
            player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cYou deleted a RandomBox : "+id));
        }
        if (action.equalsIgnoreCase("list")){
            for (RandomBox randomBox : PluginManager.getInstance().getRandomBoxManager().getRandomBoxes()){
                player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&8* "+randomBox.getID()));
            }
        }
    }
    @Subcommand("editdropparty")
    @CommandPermission("redstonepvp.command.droppaty")
    public void onDropParty(Player sender){
        sender.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&eOpenning droppaty editing menu..."));
        new DropPartyMenu(sender).open(sender);
    }
    @Subcommand("setdropparty")
    @CommandPermission("redstonepvp.command.droppaty.set")
    public void onSetDropParty(Player player){
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().set("locations.dropparty",(LocationsUtils.parseLocation(player.getTargetBlock((HashSet<Byte>) null,4).getLocation())));
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).save();
        PluginManager.getInstance().setDropParty(new DropParty(player.getTargetBlock((HashSet<Byte>) null,4).getLocation()));
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&eDropParty's new location has been assigned."));
    }
    @Subcommand("setchest")
    @CommandPermission("redstonepvp.command.chest.set")
    public void onSetChest(Player player){
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().set("locations.parkour-chest",(LocationsUtils.parseLocation(player.getTargetBlock((HashSet<Byte>) null,4).getLocation())));
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).save();
        PluginManager.getInstance().getChestManager().setChest(player.getTargetBlock((HashSet<Byte>) null,4).getLocation());
        player.sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&eParkour Chest's new location has been assigned."));
    }
}
