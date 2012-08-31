package me.supermaxman.commandcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandCraft extends JavaPlugin implements Listener{
	
	//Required
	
	public static Map<String, Location> database = new HashMap<String, Location>();
	public static CommandCraft plugin;
	public static FileConfiguration config;
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Permission permission = null;
    
    private boolean setupPermissions(){
       RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
       if (permissionProvider != null) {
           permission = permissionProvider.getProvider();
       }
       return (permission != null);
   }
    
	@Override
	public void onDisable() {this.logger.info("CommandCraft Disabled.");
	}
	
	
	
	@Override
	public void onEnable() {
		setupPermissions();		
		getServer().getPluginManager().registerEvents(new CommandCraft(), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		
		this.logger.info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			if (command.getName().equalsIgnoreCase("craft")) {
				Player p = (Player) sender;
				if (permission.has(p, "commandcraft.craft")) {
					p.openWorkbench(new Location(p.getWorld(), 0,0,0), true);
				}
			}else if (command.getName().equalsIgnoreCase("ench")) {
				Player p = (Player) sender;
				if (permission.has(p, "commandcraft.enchtable")) {
					p.openEnchanting(new Location(p.getWorld(), 0,0,0), true);
				}
			}else if (command.getName().equalsIgnoreCase("enderchest")&&
						command.getName().equalsIgnoreCase("end")&&
						command.getName().equalsIgnoreCase("endchest")) {
				Player p = (Player) sender;
				if (permission.has(p, "commandcraft.enderchest")) {
					p.openInventory(p.getEnderChest());
				}
			}
		}
        
        return true;
	}
	
	
	
}