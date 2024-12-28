package me.drman.redstonepvp.providers;

import lombok.Getter;
import net.brcdev.gangs.gang.Gang;
import me.drman.RedPvP;
import me.drman.redstonepvp.enums.ConfigurationType;
import me.drman.redstonepvp.enums.ConnectionState;
import me.drman.redstonepvp.enums.DataStorageType;
import me.drman.redstonepvp.objects.YamlConfigLoader;
import me.drman.redstonepvp.wrappers.ConfigWrapper;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

public class DataProvider {
    private final YamlConfigLoader defaultConfiguration = ConfigWrapper.valueOf(ConfigurationType.DEFAULT);
    private final String host = defaultConfiguration.getString("Storage.host"),
            database = defaultConfiguration.getString("Storage.database"),
            password = defaultConfiguration.getString("Storage.password"),
            username = defaultConfiguration.getString("Storage.username");
    private final int port = defaultConfiguration.getInt("Storage.port");
    @Getter private final DataStorageType dataStorageType = DataStorageType.valueOf(defaultConfiguration.getString("Storage.type").toUpperCase());

    @Getter private Connection connection;

    public void initialize(){
        update("CREATE TABLE IF NOT EXISTS RedPvP_data "
                + "(UUID VARCHAR(100), Kills BIGINT(100), Deaths BIGINT(100), Streak BIGINT(100), MaxStreak BIGINT(100), PRESTIGE VARCHAR(100))");
        update("CREATE TABLE IF NOT EXISTS RedPvP_gang (GANG VARCHAR(100), INVENTORY TEXT)");
    }
    public ConnectionState connect() {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                return ConnectionState.ALREADY_CONNECTED;
            }
            synchronized (this) {
                if (dataStorageType == DataStorageType.MYSQL) {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true&characterEncoding=utf8&useSSL=false&useUnicode=true", this.username, this.password);
                    initialize();
                } else if(dataStorageType == DataStorageType.SQLITE){
                    File dataFolder = new File(RedPvP.getPlugin().getDataFolder(), this.database+".db");
                    if (!dataFolder.exists()) {
                        try {
                            dataFolder.createNewFile();
                        } catch (IOException e) {
                            RedPvP.getPlugin().getLogger().log(Level.WARNING, "Failed to create "+this.database+".db: " + e.getMessage());
                        }
                    }
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
                    initialize();
                }
                return ConnectionState.SUCCESS;
            }
        } catch (SQLException | ClassNotFoundException e) {
            RedPvP.getPlugin().getLogger().log(Level.WARNING, "Couldn't connect to database server. Connection Failed");
            return ConnectionState.FAILURE;
        }
    }

    public void update(String query) {
        if (connect() != ConnectionState.FAILURE) {
            try {
                getConnection().prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet execute(String query) {
        if (connect() != ConnectionState.FAILURE) {
            try {
                ResultSet set = getConnection().createStatement().executeQuery(query);
                return set;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        RedPvP.getPlugin().getLogger().log(Level.WARNING, "Couldn't execute query. Connection Failed");
        return null;
    }

    public void register(UUID uuid) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO RedPvP_data (UUID, Kills, Deaths, Streak,PRESTIGE) VALUES (?, ?, ?, ?,?)");
            statement.setString(1, uuid.toString());
            statement.setLong(2, 0L);
            statement.setLong(3, 0L);
            statement.setLong(4, 0L);
            statement.setString(5, "");

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean registered(UUID uuid) {
        boolean registered = false;
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM RedPvP_data WHERE UUID='" + uuid + "'");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("UUID") != null) {
                    registered = true;
                }
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registered;
    }

    public long getKills(UUID uuid) {
        long x = -1L;
        try {
            ResultSet result = execute("SELECT * FROM RedPvP_data WHERE UUID='" + uuid + "'");
            if (result.next())
                x = result.getLong("Kills");
            result.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return x;
    }

    public long getDeaths(UUID uuid) {
        long x = -1L;
        try {
            ResultSet result = execute("SELECT * FROM RedPvP_data WHERE UUID='" + uuid + "'");
            if (result.next())
                x = result.getLong("Deaths");
            result.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return x;
    }

    public long getStreak(UUID uuid) {
        long x = -1L;
        try {
            ResultSet result = execute("SELECT * FROM RedPvP_data WHERE UUID='" + uuid + "'");
            if (result.next())
                x = result.getLong("Streak");
            result.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return x;
    }


    public void setKills(UUID uuid, long x) {
        update("UPDATE RedPvP_data SET Kills=" + x + " WHERE UUID='" + uuid + "'");
    }

    public void setDeaths(UUID uuid, long x) {

        update("UPDATE RedPvP_data SET Deaths=" + x + " WHERE UUID='" + uuid + "'");
    }

    public void setStreak(UUID uuid, long x) {
        update("UPDATE RedPvP_data SET Streak=" + x + " WHERE UUID='" + uuid + "'");
    }
    public long getMaxStreak(UUID uuid) {
        long x = -1L;
        try {
            ResultSet result = execute("SELECT * FROM RedPvP_data WHERE UUID='" + uuid + "'");
            if (result.next())
                x = result.getLong("MaxStreak");
            result.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return x;
    }

    public void setMaxStreak(UUID uuid, long x) {
        update("UPDATE RedPvP_data SET MaxStreak=" + x + " WHERE UUID='" + uuid + "'");
    }

    public void setGang(Gang gang, String inventory) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE RedPvP_gang SET INVENTORY = ? WHERE GANG = ?");
            statement.setString(1, inventory);
            statement.setString(2, gang.getName());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getGangInventory(String GANG) {
        try {
            ResultSet result = execute("SELECT INVENTORY FROM RedPvP_gang WHERE GANG = '" + GANG + "'");
            if (result.next()) {
                return result.getString("INVENTORY");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void registerGang(Gang gang,String inventory) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO RedPvP_gang (GANG, INVENTORY) VALUES (?,?)");
            statement.setString(1, gang.getName());
            statement.setString(2, inventory);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean registeredGang(Gang gang) {
        boolean registered = false;
        try {
            PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM RedPvP_gang WHERE GANG='" + gang.getName() + "'");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                if (result.getString("GANG") != null) {
                    registered = true;
                }
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registered;
    }

    public void removeGang(String GANG) {
        update("DELETE FROM RedPvP_gang WHERE GANG = '" + GANG + "'");
    }


}
