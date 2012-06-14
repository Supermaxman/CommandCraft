package me.supermaxman.commandcraft;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CommandCraft extends JavaPlugin implements Listener{
	
	//Required
	
	public static Map<String, Location> database = new HashMap<String, Location>();
	public static CommandCraft plugin;
	public static FileConfiguration config;
	public final Logger logger = Logger.getLogger("Minecraft");
	@Override
	public void onDisable() {this.logger.info("CommandCraft Disabled.");try {MySql.closeConn();} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	
	@Override
	public void onEnable() {
		setupConfig();
		try {MySql.initDB();MySql.createTables();} catch (SQLException e) {e.printStackTrace();}
		
		getServer().getPluginManager().registerEvents(new CommandCraft(), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		
		this.logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	
	void setupConfig() {
        config = getConfig();
        
        try {
            File CommandCraft = new File("plugins" + File.separator + "CommandCraft" + File.separator + "config.yml");
            CommandCraft.mkdir();
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!config.contains("cc.chest.bannedworlds")) {
            config.set("cc.chest.bannedworlds", "pvp");
        }
        if (!config.contains("cc.config.database")) {
            config.set("cc.config.database", "jdbc:mysql://localhost:3306/minecraft");
        }
        if (!config.contains("xp.config.user")) {
            config.set("cc.config.user", "root");
        }
        if (!config.contains("xp.config.password")) {
            config.set("cc.config.password", "YourAwesomePassword");
        }
        
        saveConfig();
        }
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
        if (sender instanceof Player && command.getName().equalsIgnoreCase("craft")) {
            if (sender.isOp()) {
            	CraftPlayer p = (CraftPlayer) sender;
            	p.openWorkbench(new Location(p.getWorld(), 0,0,0), true);
        }
        }else if (sender instanceof Player && command.getName().equalsIgnoreCase("ench")) {
            if (sender.isOp()) {
            	CraftPlayer p = (CraftPlayer) sender;
            	p.openEnchanting(new Location(p.getWorld(), 0,0,0), true);
        }
        }else if (sender instanceof Player && command.getName().equalsIgnoreCase("chest")) {
            if (sender.isOp()) {
            	CraftPlayer p = (CraftPlayer) sender;
            	try {MySql.createUser(p);} catch (SQLException e) {e.printStackTrace();}
            	Location chestloc = null;
            	try {chestloc =MySql.getChest(p);} catch (SQLException e) {e.printStackTrace();}
            	if(chestloc==null){
        			p.sendMessage(ChatColor.RED+"Error!");
        			return true;
            	}
            	
            	
            	if ((chestloc.getX()==0)&&(chestloc.getY()==0)&&(chestloc.getZ()==0)){
            		p.sendMessage(ChatColor.RED+"No Chest Chosen!");
            		return true;
            	}
        		
            	
            	if (((chestloc).getBlock().getState() instanceof Chest)){
            		if (!(config.get("cc.chest.bannedworlds").toString().contains((chestloc.getWorld().getName())))){
            			Chest c = (Chest) (chestloc).getBlock().getState();
            			p.openInventory(c.getInventory());
            		}else{
            			p.sendMessage(ChatColor.RED+"You are Not Allowed to Open That Chest In This World!");
            		}
            	}else{
            		p.sendMessage(ChatColor.RED+"Your Chest is Missing!");
            	}
        }
        }else if (sender instanceof Player && command.getName().equalsIgnoreCase("setchest")) {
            if (sender.isOp()) {
            	CraftPlayer p = (CraftPlayer) sender;
            	try {MySql.createUser(p);} catch (SQLException e) {e.printStackTrace();}
            	
            	if (p.getTargetBlock(null, 100).getType()==Material.CHEST){
            		Location loc = new Location(p.getWorld(), p.getTargetBlock(null, 100).getLocation().getX(),p.getTargetBlock(null, 100).getLocation().getY(),p.getTargetBlock(null, 100).getLocation().getZ());
            		
            		try {MySql.saveChest(p, loc);} catch (SQLException e) {e.printStackTrace();}
            		
            	}else{
            		p.sendMessage(ChatColor.RED+"Not a Chest!");
            	}
            	
        }
        }
        
        
        return true;
	}
	
	
	
}