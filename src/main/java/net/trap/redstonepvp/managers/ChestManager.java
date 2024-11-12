package me.drman.redstonepvp.managers;

import lombok.Getter;
import lombok.Setter;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.utils.LocationsUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChestManager {
    @Getter @Setter
    private Location chest;
    @Getter
    private List<String> commands = new ArrayList<>();

    public void loadAll(){
        commands = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getStringList("parkour-chest.commands");
    }
}
