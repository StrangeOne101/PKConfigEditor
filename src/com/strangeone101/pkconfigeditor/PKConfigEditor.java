package com.strangeone101.pkconfigeditor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.strangeone101.easygui.MenuListener;

public class PKConfigEditor extends JavaPlugin
{

	public static PKConfigEditor plugin;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		plugin = this;
		
		new ConfigCommand();
		new LangConfigCommand();
		
		new MenuListener(this);
		
		this.getLogger().info("PKConfigEditor loaded!");
	}

}
