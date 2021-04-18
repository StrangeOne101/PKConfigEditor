package com.strangeone101.pkconfigeditor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class PathAliases {
	
	private static Map<Element, List<String>> aliases = new HashMap<Element, List<String>>();
	private static List<String> indexPaths = new ArrayList<String>();
	
	static {
		indexPaths.add("ExtraAbilities");
		indexPaths.add("/JedCore/config.yml::Abilities");
		//indexPaths.add("/ElementumCore/config.yml::Abilities");
		
		index();
	}
	
	private static void index() {
		aliases.clear();
		
		for (String basePath : indexPaths) {
			FileConfiguration config = ConfigManager.defaultConfig.get();
			String path = basePath;
			
			if (basePath.contains("::")) {
				File file = new File(PKConfigEditor.plugin.getDataFolder().getParentFile(), basePath.split("::")[0]);
				
				if (file.exists()) {
					config = YamlConfiguration.loadConfiguration(file);
					path = basePath.split("::")[1];
					Bukkit.broadcastMessage("Found file: " + file.getAbsolutePath());
				}
			}
			
			
			for (String author : config.getConfigurationSection(path).getKeys(false)) {
				for (String ability : config.getConfigurationSection(path + author).getKeys(false)) {
					if (CoreAbility.getAbility(ability) != null) {
						Element element = CoreAbility.getAbility(ability).getElement();
						if (!aliases.containsKey(element)) {
							aliases.put(element, new ArrayList<String>());
						}
						
						aliases.get(element).add(basePath + "." + author + "." + ability);
					}
				}
			}
			
		}
		
	}
	
	public Collection<String> getAliases(Element element) {
		List<String> list = new ArrayList<String>();
		if (!(element instanceof SubElement)) {
			for (SubElement sub : Element.getSubElements(element)) {
				list.addAll(getAliases(sub));
			}
		}
		list.addAll(aliases.get(element));
		return list;
	}
	
	public void addIndex(String string) {
		indexPaths.add(string);
		index();
	}

}
