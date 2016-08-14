package com.strangeone101.pkconfigeditor;

import com.projectkorra.projectkorra.configuration.ConfigManager;

public class LangConfigCommand extends ConfigCommandBase {

	public LangConfigCommand() {
		super(ConfigManager.languageConfig, "langconfig", "/bending langconfig <get/set/add/gui> [...]", "Allows the user to change the language config in game", new String[] {"lconfig", "lc", "langc", "languageconfig"});
	}
}
