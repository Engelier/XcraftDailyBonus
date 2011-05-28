package de.xcraft.engelier.dailybonus;

import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import com.nijikokun.register.payment.Method.MethodAccount;

public class XcraftDailyBonusPlayerListener extends PlayerListener implements Listener {

	private XcraftDailyBonus plugin = null;
	private Logger log = Logger.getLogger("Minecraft");
	
	public XcraftDailyBonusPlayerListener (XcraftDailyBonus instance) {
		plugin = instance;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (plugin.pluginPermissions == null || plugin.ecoMethod == null) {
			return;
		}
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		String world = plugin.config.get("global", "world", null);
		
		if (world == null) {
			log.severe(plugin.getNameBrackets() + "default world not found in config!");
			return;
		}
		
		String playerGroup = plugin.pluginPermissions.getGroup(world, playerName);
		
		Integer amount = new Integer(plugin.config.get(playerGroup, "amount", "-1"));
		
		if (amount < 0) {
			log.warning(plugin.getNameBrackets() + "No config found for group: " + playerGroup);
		}
		
		if (amount > 0) {
			// check if player already got it's bonus today
			if (plugin.playerBase.getInt(playerName + ".day", 0) < Calendar.getInstance().get(Calendar.DATE)
					|| plugin.playerBase.getInt(playerName + ".month", 0) < Calendar.getInstance().get(Calendar.MONTH) + 1	
					|| plugin.playerBase.getInt(playerName + ".year", 0) < Calendar.getInstance().get(Calendar.YEAR)) {	
					
				// do the actual funding
				if (plugin.ecoMethod.hasAccount(playerName)) {
					MethodAccount playerAccount = plugin.ecoMethod.getAccount(playerName);
					if (playerAccount != null) {
						playerAccount.add(amount);
						
						plugin.playerBase.setProperty(playerName + ".day", Calendar.getInstance().get(Calendar.DATE));
						plugin.playerBase.setProperty(playerName + ".month", (Calendar.getInstance().get(Calendar.MONTH) + 1));
						plugin.playerBase.setProperty(playerName + ".year", (Calendar.getInstance().get(Calendar.YEAR)));
						
						log.info(plugin.getNameBrackets() + "First login of " + playerName + "(" + playerGroup + ")! Granting " + plugin.ecoMethod.format(amount));
						
						String greeting = plugin.config.get("global", "greeting", "Hey <player>! You just got <amount> for logging in today.");
						greeting = greeting.replaceAll("<group>", playerGroup);
						greeting = greeting.replaceAll("<player>", playerName);
						greeting = greeting.replaceAll("<amount>", plugin.ecoMethod.format(amount));
						player.sendMessage(ChatColor.GOLD + greeting);
					}
				}
			}
		}
	}
}
