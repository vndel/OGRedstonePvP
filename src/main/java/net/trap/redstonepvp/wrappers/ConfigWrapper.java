package me.drman.redstonepvp.wrappers;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.YamlConfigLoader;

public class ConfigWrapper {
    public static YamlConfigLoader valueOf(ConfigurationType type){
        return PluginManager.getInstance().getConfigurationManager().get(type);
    }
}