package me.drman.redstonepvp.listener;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConnectionState;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.RedPlayer;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    protected void onPlayerLoginEvent(PlayerLoginEvent event) {
        RedPlayer miner = new RedPlayer(event.getPlayer());
        RedPvP.getPlugin().getPlayerManager().register(miner);
        if (!miner.hasData()) {
            if (RedPvP.getPlugin().getDataProvider().connect() != ConnectionState.FAILURE) {
                RedPvP.getPlugin().getDataProvider().register(event.getPlayer().getUniqueId());
            }
        }
        miner.load();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        RedPvP.getPlugin().getPlayerManager().get(event.getPlayer()).assignTier();

        Location spawn = PluginManager.getInstance().getSpawnLocation();
        if (spawn != null) {
            event.getPlayer().getPlayer().teleport(spawn);
            return;
        }
        event.getPlayer().sendMessage(MessagesUtils.format("&cSpawn isn't set please report this to a staff member."));
    }
}