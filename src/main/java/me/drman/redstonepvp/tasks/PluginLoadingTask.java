package me.drman.redstonepvp.tasks;

import fr.mrmicky.fastinv.FastInvManager;
import me.drman.RedPvP;
import me.drman.redstonepvp.commands.*;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.enums.ConnectionState;
import me.drman.redstonepvp.expansions.RedstonePvPExpansion;
import me.drman.redstonepvp.listener.*;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.DropParty;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;

public class PluginLoadingTask extends Thread{

    @Override
    public void run(){
        YamlConfigLoader defaultConfig = ConfigWrapper.valueOf(ConfigurationType.DEFAULT);
        simpleConfigLoader("axesCategory","Categories");
        simpleConfigLoader("armorsCategory","Categories");
        MessagesUtils.send("&eRegistering Commands....");
        RedPvP.getPlugin().getBukkitCommandHandler().register(new RedstonePvPCommand(),new SpawnCommand(), new TiersCommand(), new ShopCommand(), new SpectateCommand(), new AdminCommand(),new BuildCommand(),new StatsCommand(),new GangChestCommand());

        PluginManager.getInstance().register(
                new PlayerJoin(),
                new PlayerInteract(),
                new PlayerChat(),
                new PlayerVelocity(),
                new PlayerCommandPreprocess(),
                new PlayerDeath(),
                new PlayerQuit(),
                new PlayerDamage(),
                new PlayerItemDrop(),
                new BlockBreak(),
                new InventoryClose(),
                new PlayerMove());
        PluginManager.getInstance().getChestManager().loadAll();
        MessagesUtils.send("&aCommands are registered successfully");
        MessagesUtils.send("&eLoading RandomBoxes....");
        PluginManager.getInstance().getRandomBoxManager().loadAll();
        MessagesUtils.send("&c"+PluginManager.getInstance().getRandomBoxManager().getRandomBoxes().size()+" &aRandomBox Loaded!");
        MessagesUtils.send("&eLoading Tiers....");
        PluginManager.getInstance().getTierManager().loadAll();
        MessagesUtils.send("&c"+PluginManager.getInstance().getTierManager().getManager().size()+" &aTier Loaded!");
        PluginManager.getInstance().getAnvilsManager().loadAll();
        MessagesUtils.send("&eLoading DropParty Items....");
        try {
            PluginManager.getInstance().setDropParty(new DropParty(LocationsUtils.deParseLocation(ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getString("locations.dropparty"))));
        }catch (Exception ignored){}
        PluginManager.getInstance().getDropPartyManager().loadItems();
        MessagesUtils.send("&c"+PluginManager.getInstance().getDropPartyManager().getItems().size()+" &aItem Loaded!");
        MessagesUtils.send("&ePreparing menus & guis....");
        FastInvManager.register(RedPvP.getPlugin());
        MessagesUtils.send("&aAll game menus has been loaded");
        try {
            PluginManager.getInstance().setSpawnLocation(LocationsUtils.deParseLocation(defaultConfig.getString("locations.spawn-point")));
            PluginManager.getInstance().getChestManager().setChest(LocationsUtils.deParseLocation(defaultConfig.getString("locations.parkour-chest")));

        }catch (Exception ignored){}
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new RedstonePvPExpansion().register();
            Bukkit.getConsoleSender().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&ePlaceholders has been loaded successfully."));
        }else{
            Bukkit.getConsoleSender().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix()+"&cFailed to locate 'PlaceholderAPI', the plugin has been disabled."));
            Bukkit.shutdown();
        }
        reRegisterPlayers();
        MessagesUtils.send("&ePreparing shop config....");
        Bukkit.getScheduler().scheduleSyncDelayedTask(RedPvP.getPlugin(),() -> {
            MessagesUtils.send("&eShop categories....");
            PluginManager.getInstance().getShopManager().loadAll();
            PluginManager.getInstance().getShopManager().loadDiscounts();
            MessagesUtils.send("&aShop loading has finished");
        },5*20);
    }
    public static void simpleConfigLoader(String name, String folder) {
        MessagesUtils.send("&aQuick load for: &e" + name);
        File defaultMaterial = new File(RedPvP.getPlugin().getDataFolder() + "/" + folder, name + ".yml");

        if (!defaultMaterial.exists()) {
            try {
                if (!new File(RedPvP.getPlugin().getDataFolder(), folder).exists()) {
                    new File(RedPvP.getPlugin().getDataFolder(), folder).mkdirs();
                }

                defaultMaterial.createNewFile();
                InputStream inputStream = RedPvP.getPlugin().getResource(name + ".yml");
                OutputStream outputStream = new FileOutputStream(defaultMaterial);
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reRegisterPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()){

            RedPvP.getPlugin().getPlayerManager().register(new RedPlayer(p));
            RedPlayer miner = RedPvP.getPlugin().getPlayerManager().get(p);
            if (!miner.hasData()) {
                if (RedPvP.getPlugin().getDataProvider().connect() != ConnectionState.FAILURE) {
                    RedPvP.getPlugin().getDataProvider().register(p.getUniqueId());
                }
            }
            miner.load();
        }
    }

}
