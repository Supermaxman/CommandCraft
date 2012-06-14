package me.supermaxman.commandcraft;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;


public class MySql {

    static FileConfiguration config = CommandCraft.config;
    static String url = config.getString("xp.config.database");
    static String user = config.getString("xp.config.user");
    static String pass = config.getString("xp.config.password");
    static Connection conn = null;
    public static void initDB() throws SQLException {
        conn = DriverManager.getConnection(url, user, pass); //Creates the connection
    }
    public static void closeConn() throws SQLException {
        conn.close();
    }
    
    //done
    public static void createTables() throws SQLException {
        PreparedStatement Statement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `CommandCraft` (`User` varchar(50) NOT NULL, `ChestWorld` varchar(100) NOT NULL, `ChestX` int(10000000) NOT NULL, `ChestY` int(10000000) NOT NULL, `ChestZ` int(10000000) NOT NULL");
        Statement.executeUpdate(); //Executes the query
        Statement.close(); //Closes the query
    }
    
    public static void saveChest(Player p, Location i) throws SQLException {
        PreparedStatement Statement = conn.prepareStatement("UPDATE `CommandCraft` SET ChestWorld='" + i.getWorld().getName() +"' AND SET ChestX='" + i.getX() + "' AND SET ChestY='" + i.getY() +"' AND SET ChestZ='" + i.getZ() + "' WHERE User='" + p.getName() + "';");
        Statement.executeUpdate();
        Statement.close();
    }
    
    //done
    public static Location getChest(Player p) throws SQLException {
        Statement state = conn.createStatement();
        ResultSet rs = state.executeQuery("SELECT ChestWorld FROM `CommandCraft` WHERE User='" + p.getName() + "';");
        ResultSet rsx = state.executeQuery("SELECT ChestX FROM `CommandCraft` WHERE User='" + p.getName() + "';");
        ResultSet rsy = state.executeQuery("SELECT ChestY FROM `CommandCraft` WHERE User='" + p.getName() + "';");
        ResultSet rsz = state.executeQuery("SELECT ChestZ FROM `CommandCraft` WHERE User='" + p.getName() + "';");
        World world = null;
        int x = 0;
        int y = 0;
        int z = 0;
        if (rs.next()) {
            world = p.getServer().getWorld(rs.getString("ChestWorld"));
        }
        if (rsx.next()) {
            x = rsx.getInt("ChestX");
        }
        if (rsy.next()) {
            y = rsy.getInt("ChestY");
        }
        if (rsz.next()) {
            z = rsz.getInt("ChestZ");
        }
        
        state.close();
        Location location = new Location(world, x, y, z);
        return location;
    }
    

    private static ArrayList<Player> seen = new ArrayList<Player>();
    public static void createUser(Player player) throws SQLException {
        if(seen.contains(player)){return;}else{seen.add(player);} //Basic cache!
        Statement state = conn.createStatement();
        final ResultSet rs = state.executeQuery("SELECT * FROM `CommandCraft` WHERE User='" + player.getName() + "';");
        if (rs.first()) {
            return;
        } else {
            PreparedStatement Statement1 = conn.prepareStatement("INSERT INTO `CommandCraft` (`User`, `ChestWorld`, `ChestX`, `ChestY`, `ChestZ`) VALUES ('" + player.getName() + "', '"+ player.getWorld().getName() +"', '0', '0', '0');"); //Put your query in the quotes
            Statement1.executeUpdate();
            Statement1.close();
            //INSERT INTO `XBank` (`id`, `User`, `Balance`) VALUES (NULL, 'TehRainbowGuy', '0');
        }
        state.close();
    }
    

}