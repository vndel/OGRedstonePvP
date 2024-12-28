package me.drman.redstonepvp.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationsUtils {

    public static String parseLocation(Location loc) {
        if (loc==null) return null;
        return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
    }

    public static Location deParseLocation(String s) {
        if (s==null) return null;
        String[] values = s.split(",");
        return new Location(Bukkit.getWorld(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]), Double.valueOf(values[3]), Float.valueOf(values[4]), Float.valueOf(values[5]));
    }
    public static Location LocationCentralize(Location location){
        double x = location.getX();
        double z = location.getZ();
        return new Location(location.getWorld(), Math.round(x) + 0.5, location.getY(), Math.round(z) + 0.5);
    }
    public static Location LocationCentralize(String location){
        return LocationCentralize(deParseLocation(location));
    }
}