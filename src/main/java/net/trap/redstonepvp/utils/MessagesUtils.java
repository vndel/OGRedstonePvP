package me.drman.redstonepvp.utils;

import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.wrappers.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Base64;

public class MessagesUtils {
    public static String format(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }
    public static String createProgressBar(int percent,int size,String s) {
        StringBuilder sb = new StringBuilder(MessagesUtils.format("&7["));
        int numChars = (int) (percent / (100.0 / size));

        for (int i = 0; i < numChars; i++) {
            sb.append(format("&b"+s));
        }
        for (int i = numChars; i < size; i++) {
            sb.append(format("&7"+s));
        }
        sb.append(MessagesUtils.format("&7]"));
        return sb.toString();
    }

    public static void send(String s){
        Bukkit.getConsoleSender().sendMessage(format(RedPvP.getPlugin().getPrefix()+s));
    }
    public static String getMessage(String path){
        return ConfigWrapper.valueOf(ConfigurationType.MESSAGE).getString(path).replace("%prefix%", RedPvP.getPlugin().getPrefix());
    }
    public static String timeFormat(int seconds) {
        int days = seconds / 86400; // 86400 seconds in a day
        seconds %= 86400;
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int seconds_output = (seconds % 3600) % 60;

        if (days > 0) {
            return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds_output);
        } else if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds_output);
        } else {
            return String.format("%dm %ds", minutes, seconds_output);
        }
    }
}