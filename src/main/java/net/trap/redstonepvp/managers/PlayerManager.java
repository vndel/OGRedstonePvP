package me.drman.redstonepvp.managers;

import lombok.Getter;
import me.drman.redstonepvp.objects.RedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager
{
    @Getter
    private final HashMap<Player, RedPlayer> manager = new HashMap<>();

    public RedPlayer get(Player player) {
        if (contains(player)) {
            return getManager().get(player);
        }
        return null;
    }

    public RedPlayer get(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (contains(player)) {
            return getManager().get(player);
        }
        return null;
    }

    public RedPlayer register(RedPlayer miner){
        if(contains(miner)) return null;
        return getManager().put(miner.getPlayer(),miner);
    }
    public boolean contains(Player player){
        return getManager().containsKey(player);
    }

    public boolean contains(RedPlayer miner){
        return getManager().containsValue(miner);
    }
    public RedPlayer unregister(Player player){
        if(!contains(player)) return null;
        return getManager().remove(player);
    }
}