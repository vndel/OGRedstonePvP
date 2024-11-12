package me.drman.redstonepvp.managers;
import com.google.common.collect.Maps;
import lombok.Getter;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.objects.YamlConfigLoader;

import java.util.Map;

public class ConfigurationManager
{
    @Getter private final Map<ConfigurationType, YamlConfigLoader> manager = Maps.newHashMap();

    public YamlConfigLoader get(ConfigurationType type) {
        if (getManager().containsKey(type)) {
            return getManager().get(type);
        }
        RedPvP.getPlugin().getLogger().warning("Failed to load configuration type " + type);
        return null;
    }

    public YamlConfigLoader register(YamlConfigLoader configuration){
        if(getManager().containsKey(configuration.getConfigurationType())) return null;
        return getManager().put(configuration.getConfigurationType(),configuration);
    }
    public boolean contains(ConfigurationType type){
        return getManager().containsKey(type);
    }
    public boolean contains(YamlConfigLoader configuration){
        return getManager().containsValue(configuration);
    }

}