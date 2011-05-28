package me.Engelier.XcraftDailyBonus;

import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.nijikokun.register.payment.Methods;

public class XcraftDailyBonusPluginListener extends ServerListener implements Listener {
	private XcraftDailyBonus plugin = null;
	private Methods Methods = null;
	
	public XcraftDailyBonusPluginListener (XcraftDailyBonus instance) {
		plugin = instance;
		Methods = new Methods();
	}

	public void checkForPlugins() {
		PluginManager pm = plugin.getServer().getPluginManager();
		
		Plugin checkPermissions = pm.getPlugin("Permissions");
		if (checkPermissions != null) {
			if (checkPermissions.isEnabled()) {
				plugin.setPluginPermissions(((Permissions)checkPermissions).getHandler());
			}
		}		
	}
	
	public void onPluginEnable(PluginEnableEvent event) {
		Plugin thisPlugin = event.getPlugin();
		
		if (!Methods.hasMethod()) {
			if (Methods.setMethod(thisPlugin)) {
				plugin.ecoMethod = Methods.getMethod();
				System.out.println(plugin.getNameBrackets() + "Payment method found (" + plugin.ecoMethod.getName() + " version: " + plugin.ecoMethod.getVersion() + ")");
			}
		}
		
		if (thisPlugin.getDescription().getName().equals("Permissions")) {
			plugin.setPluginPermissions(((Permissions)thisPlugin).getHandler());
		}
	}
	
	public void onPluginDisable(PluginDisableEvent event) {
		Plugin thisPlugin = event.getPlugin();
		
		if (Methods != null && Methods.hasMethod()) {
			Boolean check = Methods.checkDisabled(thisPlugin);
			
			if (check) {
				plugin.ecoMethod = null;
				System.out.println(plugin.getNameBrackets() + "Payment method was disabled. Disabling.");
			}
		}
		
		if (thisPlugin.getDescription().getName().equals("Permissions")) {
			plugin.setPluginPermissions(null);
		}		
	}
}
