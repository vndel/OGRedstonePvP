package me.drman;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PlayerManager;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RandomBox;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.providers.DataProvider;
import me.drman.redstonepvp.tasks.*;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class RedPvP extends JavaPlugin {

    @Getter
    private static RedPvP plugin;
    private final PluginManager pluginManager;
    private final String prefix;
    private final DataProvider dataProvider;
    private final PlayerManager playerManager;
    private BukkitCommandHandler bukkitCommandHandler;
    private Economy economy;

    public RedPvP() {
        plugin = this;
        this.pluginManager = new PluginManager();
        pluginManager.register(
                new YamlConfigLoader(plugin, "config", ConfigurationType.DEFAULT),
                new YamlConfigLoader(plugin, "messages", ConfigurationType.MESSAGE),
                new YamlConfigLoader(plugin,"tiers",ConfigurationType.TIERS),
                new YamlConfigLoader(plugin,"dropparty",ConfigurationType.DROPPARTY),
                new YamlConfigLoader(plugin,"shop",ConfigurationType.SHOP),
                new YamlConfigLoader(plugin,"axes",ConfigurationType.AXES)

        );
        this.prefix = ConfigWrapper.valueOf(ConfigurationType.MESSAGE).getString("prefix");
        this.dataProvider = new DataProvider();
        this.playerManager = new PlayerManager();
    }
    @Override
    public void onEnable() {
            if (!setupEconomy()) {
                getLogger().severe(String.format(prefix + "Disabled due to no Vault dependency found!", getDescription().getName()));
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        this.bukkitCommandHandler = BukkitCommandHandler.create(this);
        new PluginLoadingTask().run();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new TierTrackingTask(),0,20);
        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(this,new DropPartyTask(),0,20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CombatLogTask(), 0, 20);
        PluginManager.getInstance().getDropPartyManager().setTaskId(task);
        if (PluginManager.getInstance().getSpawnLocation()!=null) {
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                PluginManager.getInstance().getSpawnLocation().getWorld().setTime(12044);
            }, 0L, 200L);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onDisable() {
        int i = 0;
        ConfigWrapper.valueOf(ConfigurationType.DROPPARTY).getConfiguration().set("Items",null);
        for (ItemStack item : PluginManager.getInstance().getDropPartyManager().getItems()){
            ItemsUtils.setItem(item,"Items."+i,ConfigWrapper.valueOf(ConfigurationType.DROPPARTY).getConfig());
            i++;
        }
        ConfigWrapper.valueOf(ConfigurationType.DROPPARTY).save();
        MessagesUtils.send("&3Saving randomboxes data..");
        for (RandomBox box : PluginManager.getInstance().getRandomBoxManager().getRandomBoxes()){
            PluginManager.getInstance().getRandomBoxManager().saveBox(box);
        }
        MessagesUtils.send("&aRandomBoxes data saved!.");
        MessagesUtils.send("&3Saving dropparty items..");
        for (RedPlayer redPlayer : getPlayerManager().getManager().values()){
            redPlayer.save();
        }
        MessagesUtils.send("&aDropParty items saved!");
        List<String> repairLocations = new ArrayList<>();
        List<String> mergeLocations = new ArrayList<>();
        for (Location s : PluginManager.getInstance().getAnvilsManager().getRepairAnvils()){
            repairLocations.add(LocationsUtils.parseLocation(s));
        }
        for (Location s : PluginManager.getInstance().getAnvilsManager().getMergeAnvils()){
            mergeLocations.add(LocationsUtils.parseLocation(s));
        }
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().set("anvils.repair",repairLocations);
        ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfig().set("anvils.merge",mergeLocations);
        saveConfig();
    }
}