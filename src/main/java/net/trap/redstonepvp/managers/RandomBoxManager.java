package me.drman.redstonepvp.managers;

import lombok.Data;
import lombok.Getter;
import me.drman.RedPvP;
import me.drman.redstonepvp.builders.ItemBuilder;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.objects.RandomBox;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.utils.ItemsUtils;
import me.drman.redstonepvp.utils.JsonUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data public class RandomBoxManager {

    private List<RandomBox> randomBoxes=new ArrayList<>();
    private List<ItemStack> items = new ArrayList<>();

    public void loadAll(){
        YamlConfigLoader defaultConfig = ConfigWrapper.valueOf(ConfigurationType.DEFAULT);
        RandomBox.setPrice(new ItemBuilder(Material.getMaterial(defaultConfig.getString("RandomBox.price").split(":")[0])).amount(
                Integer.valueOf(defaultConfig.getString("RandomBox.price").split(":")[1])
        ).build());
        for (String s : defaultConfig.getConfiguration().getConfigurationSection("RandomBox.items").getKeys(false)){
            PluginManager.getInstance().getRandomBoxManager().getItems().add(ItemsUtils.getItem("RandomBox.items."+s,defaultConfig.getConfig()));
        }
        File dir = new File(RedPvP.getPlugin().getDataFolder()+"/RandomBoxes");
        if (!dir.exists()){
            dir.mkdir();
        }else {
            for (File file : dir.listFiles()){
                RandomBox box = JsonUtils.read(RedPvP.getPlugin(),file.getName().replace(".json",""),RandomBox.class);
                PluginManager.getInstance().getRandomBoxManager().addBox(box);
            }
        }
    }
    public void addBox(RandomBox box) {
        randomBoxes.add(box);
    }

    public void removeBox(RandomBox box) {
        randomBoxes.remove(box);
    }

    public void saveBox(RandomBox box) {
        JsonUtils.write(RedPvP.getPlugin(), "" + box.getID(), box);
    }

    public void destroyBox(RandomBox box) {
        randomBoxes.remove(box);
        File file = new File(RedPvP.getPlugin().getDataFolder() + "/RandomBoxes/" + box.getID() + ".json");
        file.deleteOnExit();
    }

    public Optional<RandomBox> getRandomBoxByID(int id) {
        return randomBoxes.stream().filter(box -> box.getID() == id).findFirst();
    }

    public void clearAllBoxes() {
        randomBoxes.clear();
    }
}
