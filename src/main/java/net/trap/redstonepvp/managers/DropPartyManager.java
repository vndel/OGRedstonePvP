package me.drman.redstonepvp.managers;

import lombok.Getter;
import lombok.Setter;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DropPartyManager {

    @Setter private List<ItemStack> items = new ArrayList<>();
    @Getter @Setter private int taskId;

    public void loadItems(){
        try {
            for (String s : ConfigWrapper.valueOf(ConfigurationType.DROPPARTY).getConfiguration().getConfigurationSection("Items").getKeys(false)) {
                items.add(ItemsUtils.getItem("Items." + s, ConfigWrapper.valueOf(ConfigurationType.DROPPARTY).getConfiguration()));
            }
        }catch (Exception e){

        }
    }

}
