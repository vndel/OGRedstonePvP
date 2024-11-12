package me.drman.redstonepvp.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brcdev.gangs.GangsPlusApi;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.utils.MessagesUtils;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    /*@EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getMessage().contains("%")) {
            event.setCancelled(true);
            return;
        }
        String format;
        if (GangsPlusApi.isInGang(event.getPlayer())) {
            format = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getString("chat-format.in-gang-chat");
        } else {
            format = ConfigWrapper.valueOf(ConfigurationType.DEFAULT).getString("chat-format.normal-chat");
        }

        if (format != null) {
            String message = event.getMessage();
            String formattedMessage = MessagesUtils.format(PlaceholderAPI.setPlaceholders(event.getPlayer(), format.replace("%message%", message)));
            event.setFormat(formattedMessage); // Set the formatted message as the chat format
        } else {
            event.setCancelled(true); // Handle the case where format is null
        }
    }*/


}
