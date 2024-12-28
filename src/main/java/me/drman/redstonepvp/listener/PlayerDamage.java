package me.drman.redstonepvp.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) e.setCancelled(true);
    }

}
