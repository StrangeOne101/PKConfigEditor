package com.strangeone101.pkconfigeditor;

import org.bukkit.plugin.java.JavaPlugin;

public class PKConfigEditor extends JavaPlugin
{

	public static PKConfigEditor plugin;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		plugin = this;
		
		new ConfigCommand();
		new LangConfigCommand();
		
		this.getLogger().info("PKConfigEditor loaded!");
	}

}
