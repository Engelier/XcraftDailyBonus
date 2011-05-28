package me.Engelier.XcraftDailyBonus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class XcraftDailyBonusConfig {
	private XcraftDailyBonus plugin;
	private Yaml yaml;
	private Map<String, Object> config = new HashMap<String, Object>();
		
	public XcraftDailyBonusConfig (XcraftDailyBonus instance) {
		plugin = instance;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean load() {
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		
		if (!configFile.exists()) {
			plugin.getDataFolder().mkdir();
			plugin.getDataFolder().setWritable(true);
			plugin.getDataFolder().setExecutable(true);		

			try {
				configFile.createNewFile();
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		yaml = new Yaml();
		try {
			config = (Map<String, Object>) yaml.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}		
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public String get(String section, String key) {
		Map<String, String> configSection = (Map<String, String>) config.get(section);
		if (configSection != null) {
			Object value = configSection.get(key);
			
			return value.toString();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public String get(String section, String key, String defaultValue) {
		Map<String, String> configSection = (Map<String, String>) config.get(section);
		if (configSection != null) {
			Object value = configSection.get(key);
			
			return value.toString();
		} else {
			return defaultValue;
		}
	}
}
