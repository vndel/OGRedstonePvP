package me.drman.redstonepvp.managers;

import lombok.Getter;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AnvilsManager {
    private final List<Location> repairAnvils = new ArrayList<>();
    private final List<Location> mergeAnvils = new ArrayList<>();

    public void loadAll(){
        for (String s : ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfiguration().getStringList("anvils.repair")){
            repairAnvils.add(LocationsUtils.deParseLocation(s));
        }
        for (String s : ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getConfiguration().getStringList("anvils.merge")){
            mergeAnvils.add(LocationsUtils.deParseLocation(s));
        }
    }
    public boolean isRepairAnvil(Location location){
        return repairAnvils.contains(location);
    }
    public boolean isMergeAnvil(Location location){
        return mergeAnvils.contains(location);
    }

}
