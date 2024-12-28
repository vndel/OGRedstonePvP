package me.drman.redstonepvp.managers;

import lombok.Getter;
import lombok.Setter;
import me.realized.duels.api.Duels;
import me.drman.RedPvP;
import me.drman.redstonepvp.objects.DropParty;
import me.drman.redstonepvp.objects.RandomBox;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

@Getter public class PluginManager {
    @Getter private static PluginManager instance;
    private final ConfigurationManager configurationManager = new ConfigurationManager();
    private final TiersManager tierManager = new TiersManager();
    private final CombatManager combatManager = new CombatManager();
    private final DropPartyManager dropPartyManager = new DropPartyManager();
    private final ShopManager shopManager = new ShopManager();
    private final ChestManager chestManager = new ChestManager();
    private final AnvilsManager anvilsManager = new AnvilsManager();
    private final RandomBoxManager randomBoxManager = new RandomBoxManager();
    private final Map<String, Long> cooldowns = new HashMap<>();
    Duels api = (Duels) Bukkit.getServer().getPluginManager().getPlugin("Duels");

    @Getter @Setter
    private Location spawnLocation;
    @Getter @Setter private DropParty dropParty;


    public PluginManager(){
        instance = this;
    }


    public void register(Listener... listeners){
        for(Listener listener : listeners){
            Bukkit.getPluginManager().registerEvents(listener, RedPvP.getPlugin());
        }
    }
    public void register(YamlConfigLoader... configurations){
        for(YamlConfigLoader configuration : configurations){
            getConfigurationManager().register(configuration);
        }
    }
    public void register(RandomBox... randomBoxes){
        for(RandomBox randomBox : randomBoxes){
            getRandomBoxManager().addBox(randomBox);
        }
    }
}