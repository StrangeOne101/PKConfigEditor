package com.strangeone101.pkconfigeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.command.PKCommand;
import com.projectkorra.projectkorra.configuration.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class ConfigCommand extends PKCommand {

	public List<UUID> reminders;
	
	public ConfigCommand() {
		super("config", "/bending config <get/set/add/gui> [configoption] [value] OR /bending config gui", "Allows the user to change the config in game", new String[] {"config", "co"});
		this.reminders = new ArrayList<UUID>();
	}

	@Override
	public void execute(CommandSender sender, List<String> args) {
		if (!hasPermission(sender)) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
			return;
		}
		
		if (args.size() == 0 || (args.size() == 1 && !args.get(0).equalsIgnoreCase("gui"))) {
			help(sender, false);
			sender.sendMessage(ChatColor.RED + "If you need help, try pressing TAB to autocomplete the command.");
			return;
		} else if (args.get(0).equalsIgnoreCase("gui")) {
			if (!hasPermission(sender, "gui")) {
				sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
				return;
			}
			
			sender.sendMessage(ChatColor.GREEN + "This feature is coming soon! Sorry about that!");
			//TODO Make the gui
			
			return;
		} else if (args.get(0).equalsIgnoreCase("get")) {
			String path = args.get(0);
			if (ConfigManager.defaultConfig.get().contains(path)) {
				String value = ConfigManager.defaultConfig.get().get(path).toString().toUpperCase();
				sender.sendMessage(ChatColor.YELLOW + "Value of '" + path + "' is " + value);
			} else {
				sender.sendMessage(ChatColor.RED + "Config value not found! Please use TAB to autcomplete the command if you need help.");
			}
		} else if (args.get(0).equalsIgnoreCase("set")) {
			if (args.size() == 2) {
				sender.sendMessage(ChatColor.RED + "Please specify both a config option and a value to set it to!");
				return;
			}
			String path = args.get(1);
			if (ConfigManager.defaultConfig.get().contains(path)) {
				Object value = ConfigManager.defaultConfig.get().get(path);
				String input = args.get(2);
				
				if (isType(value, input)) {
					ConfigManager.defaultConfig.get().set(path, convert(value, input));
					sender.sendMessage(ChatColor.YELLOW + "Config option set!");
					if (sender instanceof Player) {
						remind((Player) sender);
					}
				} else {
					if (value instanceof Boolean) {
						sender.sendMessage(ChatColor.RED + "Config option must be set to either TRUE or FALSE!");
					} else if (value instanceof Long) {
						sender.sendMessage(ChatColor.RED + "Config option must be a big number! (Most likely a number in the 1000s)");
					} else if (value instanceof Integer) {
						sender.sendMessage(ChatColor.RED + "Config option must be a whole number! (Most likely a number higher or equals to 0 that is not decimal)");
					} else if (value instanceof Double) {
						sender.sendMessage(ChatColor.RED + "Config option must be a decimal number! (Most likely a number no bigger than 10 but no lower than 0)");
					} else {
						sender.sendMessage(ChatColor.RED + "Cannot set the value to a value different to what's in the config!");
					}
					
					return;
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Config value not found! Please use TAB to autcomplete the command if you need help.");
			}
		}
		
		
	}
	
	/**Used to compare the type of object from config files to values entered from the user*/
	protected static boolean isType(Object object, String string) {
		if (object instanceof Long || object instanceof Integer) {
			try {
				Long.parseLong(string);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else if (object instanceof Double || object instanceof Float) {
			try {
				Double.parseDouble(string);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else if (object instanceof Boolean) {
			if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("false")) return true;
			return false;
		} else if (object instanceof List) {
			return string != null;
		} else if (object instanceof String) return true;
		
		return false;
	}
	
	protected static Object convert(Object object, String input) {
		if (object instanceof Long || object instanceof Integer) {
			try {
				return Long.parseLong(input);
			} catch (NumberFormatException e) {
				return null;
			}
		} else if (object instanceof Double || object instanceof Float) {
			try {
				return Double.parseDouble(input);
			} catch (NumberFormatException e) {
				return null;
			}
		} else if (object instanceof Boolean) {
			if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) return Boolean.parseBoolean(input);
			return null;
		} else if (object instanceof List) {
			return Arrays.asList(new String[] {input});
		} else if (object instanceof String) return input;
		return null;
	}
	
	public void remind(final Player sender) {
		if (!reminders.contains(sender.getUniqueId())) {
			sender.sendMessage(ChatColor.YELLOW + "Remember to reload bending with " + ChatColor.GOLD + "/bending reload " + ChatColor.YELLOW + "in order to see the changes");
			reminders.add(sender.getUniqueId());
			
			new BukkitRunnable() {
				@Override
				public void run() {
					reminders.remove(sender.getUniqueId());
				}
			}.runTaskLater(PKConfigEditor.plugin, 20 * 60);
		}
	}
	
	@Override
	protected List<String> getTabCompletion(CommandSender sender, List<String> args) {
		if (args.size() == 0) return Arrays.asList(new String[] {"add", "get", "set", "gui"});
		
		return super.getTabCompletion(sender, args);
	}

}
