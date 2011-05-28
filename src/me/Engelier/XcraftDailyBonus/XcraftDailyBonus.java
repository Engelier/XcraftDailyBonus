package me.Engelier.XcraftDailyBonus;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.register.payment.Method;

public class XcraftDailyBonus extends JavaPlugin {

	private Logger log = Logger.getLogger("Minecraft");
	private XcraftDailyBonusPlayerListener playerListener = new XcraftDailyBonusPlayerListener(this);
	private XcraftDailyBonusPluginListener pluginListener = new XcraftDailyBonusPluginListener(this);
	
	public PermissionHandler pluginPermissions = null;
	public Method ecoMethod = null;
	
	public XcraftDailyBonusConfig config = new XcraftDailyBonusConfig(this);
	public Configuration playerBase;
	
	public void onDisable() {
		playerBase.save();
		log.info(getNameBrackets() + "unloaded.");
	}

	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLUGIN_ENABLE, pluginListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLUGIN_DISABLE, pluginListener, Event.Priority.Monitor, this);
		
		log.info(getNameBrackets() + "by Engelier loaded.");
		pluginListener.checkForPlugins();
		
		loadConfig();
		loadPlayerBase();

		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				playerBase.save();
				log.info(getNameBrackets() + "saving user data");
			}			
		}, 72000L, 72000L);
	}
	
	public void loadPlayerBase() {
		File configFile = new File(getDataFolder(), "users.yml");
		
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		playerBase = new Configuration(configFile);
		playerBase.load();
	}
	
	public void loadConfig() {
		if (!config.load()) {
			log.severe(getNameBrackets() + "unable to load config.yml");
		}
	}
	
	public void setPluginPermissions(PermissionHandler permissions) {
		pluginPermissions = permissions;
		
		if (pluginPermissions == null) {
			log.info(getNameBrackets() + "lost permissions plugin. Disabling.");
		} else {
			log.info(getNameBrackets() + "hooked into Permissions.");
		}
	}	

	public String getNameBrackets() {
		return "[" + this.getDescription().getFullName() + "] ";
	}
}
