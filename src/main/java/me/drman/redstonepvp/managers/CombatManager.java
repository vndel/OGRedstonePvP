package me.drman.redstonepvp.managers;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

@Getter
public class CombatManager
{
    private final HashMap<Player, Integer> manager = new HashMap<>();

    public Integer get(Player miner) {
        if (contains(miner)) {
            return getManager().get(miner);
        }
        return null;
    }

    public Integer register(Player miner,int i){
        if(contains(miner)) return null;
        return getManager().put(miner,i);
    }
    public boolean contains(Player miner){
        return getManager().containsKey(miner);
    }

    public boolean contains(int i){
        return getManager().containsValue(i);
    }
    public Integer unregister(Player miner){
        if(!contains(miner)) return null;
        return getManager().remove(miner);
    }
}