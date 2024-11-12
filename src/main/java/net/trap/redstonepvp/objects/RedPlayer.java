package me.drman.redstonepvp.objects;

import com.google.common.base.Preconditions;
import lombok.Data;
import me.clip.placeholderapi.PlaceholderAPI;
import me.drman.Axes;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConnectionState;
import me.drman.redstonepvp.managers.PluginManager;
import me.drman.redstonepvp.objects.shop.ShopItem;
import me.drman.redstonepvp.utils.ActionBarUtils;
import me.drman.redstonepvp.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

@Data public class RedPlayer {
    private final UUID uniqueId;
    private final Player player;
    private long kills, deaths, streak, maxStreak;
    private Tier currentTier;
    private boolean isTeleporting;
    private boolean isBuilding;
    private boolean isSpectating;

    public RedPlayer(Player player) {
        this.uniqueId = player.getUniqueId();
        this.player = player;
        this.kills = 0L;
        this.deaths = 0L;
        this.streak = 0L;
        this.maxStreak = 0;
        this.currentTier = null;
    }

    public String getName() {
        return getPlayer().getName();
    }

    public void sendAction(String s) {
        ActionBarUtils.sendActionBar(player, s);
    }

    public void equip() {

    }

    public boolean isFull() {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isInCombat() {
        return PluginManager.getInstance().getCombatManager().getManager().containsKey(player);
    }

    public void sendMessage(String message) {
        getPlayer().sendMessage(MessagesUtils.format(RedPvP.getPlugin().getPrefix() + message));
    }

    public int getDiscount() {
        int discount = 0;
        for (Map.Entry<Integer, String> entry : PluginManager.getInstance().getShopManager().getDiscounts().entrySet()) {
            if (player.hasPermission(entry.getValue())) {
                if (entry.getKey() > discount) {
                    discount = entry.getKey();
                }
            }
        }
        return discount;
    }
    public void clearInventory(){
        Location location = player.getLocation();
        for (ItemStack itemStack : player.getPlayer().getInventory().getContents()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                if (!Axes.getPlugin().getAxesManager().isAxe(itemStack)) {
                    player.getPlayer().getInventory().remove(itemStack);
                    player.getPlayer().getWorld().dropItem(location, itemStack);
                }
            }
        }
        for (ItemStack itemStack : player.getPlayer().getInventory().getArmorContents()) {
            if (itemStack != null && !itemStack.getType().equals(Material.AIR)) {
                player.getPlayer().getWorld().dropItem(location, itemStack);
            }
        }
        player.getInventory().setArmorContents(null);
    }
    public void respawn(Location location) {

        if (location == null) {
            player.sendMessage(MessagesUtils.format("&cSpawn isn't set. Please report this to a staff member."));
            return;
        }
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(location, "location");

        try {

            String bukkitVersion = Bukkit.getServer().getClass()
                    .getPackage().getName().substring(23);

            Class<?> cp = Class.forName("org.bukkit.craftbukkit."
                    + bukkitVersion + ".entity.CraftPlayer");
            Class<?> clientCmd = Class.forName("net.minecraft.server."
                    + bukkitVersion + ".PacketPlayInClientCommand");
            Class enumClientCMD = Class.forName("net.minecraft.server."
                    + bukkitVersion + ".PacketPlayInClientCommand$EnumClientCommand");

            Method handle = cp.getDeclaredMethod("getHandle");

            Object entityPlayer = handle.invoke(player);

            Constructor<?> packetConstr = clientCmd
                    .getDeclaredConstructor(enumClientCMD);
            Enum<?> num = Enum.valueOf(enumClientCMD, "PERFORM_RESPAWN");

            Object packet = packetConstr.newInstance(num);

            Object playerConnection = entityPlayer.getClass()
                    .getDeclaredField("playerConnection").get(entityPlayer);
            Method send = playerConnection.getClass().getDeclaredMethod("a",
                    clientCmd);

            send.invoke(playerConnection, packet);

            player.teleport(location);

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException |
                 NoSuchMethodException | ClassNotFoundException | NoSuchFieldException ex) {
            ex.printStackTrace();
        }
        setPlayerKiller(null);
        player.setHealth(20);
        player.setExp(0);
        player.getPlayer().setVelocity(new Vector(0, 0, 0));
    }


    public void setPlayerKiller(Player newKiller) {
        try {
            Class<?> craftLivingEntityClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".entity.CraftLivingEntity");

            Method getHandleMethod = craftLivingEntityClass.getDeclaredMethod("getHandle");

            Class<?> entityLivingClass = Class.forName("net.minecraft.server." + getVersion() + ".EntityLiving");

            Field killerField = entityLivingClass.getDeclaredField("killer");

            getHandleMethod.setAccessible(true);
            killerField.setAccessible(true);

            Object craftLivingEntity = craftLivingEntityClass.cast(player);
            Object entityLiving = getHandleMethod.invoke(craftLivingEntity);

            killerField.set(entityLiving, (newKiller != null) ? getHandleMethod.invoke(newKiller) : null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVersion() {
        return org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public void load() {
        if (RedPvP.getPlugin().getDataProvider().connect() != ConnectionState.FAILURE) {
            this.kills = RedPvP.getPlugin().getDataProvider().getKills(this.uniqueId);
            this.deaths = RedPvP.getPlugin().getDataProvider().getDeaths(this.uniqueId);
            this.streak = RedPvP.getPlugin().getDataProvider().getStreak(this.uniqueId);
            this.maxStreak = RedPvP.getPlugin().getDataProvider().getMaxStreak(this.uniqueId);
        }
    }

    public void save() {
        if (!hasData()) return;
        if (RedPvP.getPlugin().getDataProvider().connect() != ConnectionState.FAILURE) {
            RedPvP.getPlugin().getDataProvider().setKills(player.getUniqueId(), this.kills);
            RedPvP.getPlugin().getDataProvider().setDeaths(player.getUniqueId(), this.deaths);
            RedPvP.getPlugin().getDataProvider().setStreak(player.getUniqueId(), this.streak);
            RedPvP.getPlugin().getDataProvider().setMaxStreak(player.getUniqueId(), this.maxStreak);
        }
    }

    public void addKills(int i) {
        setKills(getKills() + i);
    }

    public void addDeaths(int i) {
        setDeaths(getDeaths() + i);
    }

    public void addStreak(int i) {
        setStreak(getStreak() + i);
        if (getStreak() >= getMaxStreak()) setMaxStreak(getStreak());
    }

    public void addMaxStreak(int i) {
        setMaxStreak(getMaxStreak() + i);
    }

    public double getKD() {
        if (deaths == 0) {
            return -1;
        }
        return (double) kills / deaths;
    }

    public boolean hasData() {
        if (RedPvP.getPlugin().getDataProvider().connect() != ConnectionState.FAILURE) {
            return RedPvP.getPlugin().getDataProvider().registered(uniqueId);
        }
        return false;
    }

    public int getPlayTime(){
        return Integer.valueOf(PlaceholderAPI.setPlaceholders(player,"%statistic_hours_played%"));
    }
    public int getVotes(){
        return Integer.valueOf(PlaceholderAPI.setPlaceholders(player,"%supervotes_votes%"));
    }

    public void assignTier() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RedPvP.getPlugin(),() -> {
            currentTier=PluginManager.getInstance().getTierManager().getTier(player);
        },20);
    }
    public String getChestCoolDown() {
        String time = PlaceholderAPI.setPlaceholders(player, "%luckperms_expiry_time_redstonepvp.chest.deny%");
        if (time.isEmpty()) {
            return MessagesUtils.format("&aOPEN CHEST");
        }
        return time;
    }

    public boolean inDuel() {
        return PluginManager.getInstance().getApi().getArenaManager().isInMatch(player.getPlayer());
    }
}