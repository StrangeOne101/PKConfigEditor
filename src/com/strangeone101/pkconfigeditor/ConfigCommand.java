package com.strangeone101.pkconfigeditor;

import com.projectkorra.projectkorra.configuration.ConfigManager;

public class ConfigCommand extends ConfigCommandBase {

	public ConfigCommand() {
		super(ConfigManager.defaultConfig, "config", "/bending config <get/set/add/gui> [...]", "Allows the user to change the config in game", new String[] {"config", "co"});
		
		// TODO Auto-generated constructor stub
	}

}
