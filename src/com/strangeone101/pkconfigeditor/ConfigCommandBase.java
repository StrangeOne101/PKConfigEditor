package com.strangeone101.pkconfigeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.command.PKCommand;
import com.projectkorra.projectkorra.configuration.Config;

public class ConfigCommandBase extends PKCommand {

	public List<UUID> reminders;
	public Config config;
	
	
	public ConfigCommandBase(Config config, String name, String usage, String desc, String... aliases) {
		super(name, usage, desc, aliases);
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
			if (this.config.get().contains(path)) {
				String value = this.config.get().get(path).toString().toUpperCase();
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
			if (this.config.get().contains(path)) {
				Object value = this.config.get().get(path);
				String input = args.get(2);
				
				if (isType(value, input)) {
					if (value instanceof String || value instanceof List) {
						List<String> args2 = args;
						args2.remove(0);
						args2.remove(0); //Remove first 2 args
						String s = "";
						for (String s1 : args2) {
							s = s + " " + s1;
						}
						input = s.substring(1);
					}
					this.config.get().set(path, convert(value, input));
					sender.sendMessage(ChatColor.YELLOW + "Config option set! Value is now \"" + value.toString() + "\"");
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
		} else if (args.get(0).equalsIgnoreCase("add")) {
			if (args.size() == 2) {
				sender.sendMessage(ChatColor.RED + "Please specify both a config option and a value to add!");
				return;
			}
			String path = args.get(1);
			if (this.config.get().contains(path)) {
				Object value = this.config.get().get(path);
				String input = args.get(2);
				
				if (isType(value, input)) {
					if (value instanceof Boolean) {
						sender.sendMessage(ChatColor.RED + "Cannot add to a TRUE or FALSE value!");
						return;
					} else if (value instanceof Long) {
						Long llong = (Long) value;
						llong += Long.parseLong(input);
						value = llong;
						sender.sendMessage(ChatColor.YELLOW + "Number added to config option! Value is now " + value.toString());
					} else if (value instanceof Integer) {
						Integer iint = (Integer) value;
						iint += Integer.parseInt(input);
						value = iint;
						sender.sendMessage(ChatColor.YELLOW + "Number added to config option! Value is now " + value.toString());
					} else if (value instanceof Double) {
						Double d = (Double) value;
						d += Double.parseDouble(input);
						value = d;
						sender.sendMessage(ChatColor.YELLOW + "Decimal added to config option! Value is now " + value.toString());
					} else if (value instanceof String) {
						List<String> args2 = args;
						args2.remove(0);
						args2.remove(0); //Remove first 2 args
						String s = "";
						for (String s1 : args2) {
							s = s + " " + s1;
						}
						s = s.substring(1);
						value = ((String) value) + s;
						sender.sendMessage(ChatColor.YELLOW + "Text added to config option! Value is now \"" + value.toString() + "\"");
					} else if (value instanceof List) {
						List<String> args2 = args;
						args2.remove(0);
						args2.remove(0); //Remove first 2 args
						String s = "";
						for (String s1 : args2) {
							s = s + " " + s1;
						}
						s = s.substring(1);
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>) value;
						list.add(s);
						value = list;
						sender.sendMessage(ChatColor.YELLOW + "Text added to the config option (list)!");
					} else {
						sender.sendMessage(ChatColor.RED + "Cannot set the value to a value different to what's in the config!");
						return;
					}
					this.config.get().set(path, value);
					
					if (sender instanceof Player) {
						remind((Player) sender);
					}
				} else {
					if (value instanceof Boolean) {
						sender.sendMessage(ChatColor.RED + "Cannot add to a TRUE or FALSE value!");
					} else if (value instanceof Long) {
						sender.sendMessage(ChatColor.RED + "Number being added must be a whole number (Original value is most likely in the 1000s)");
					} else if (value instanceof Integer) {
						sender.sendMessage(ChatColor.RED + "Number being added must be a whole number!");
					} else if (value instanceof Double) {
						sender.sendMessage(ChatColor.RED + "Number being added must be a decimal number!");
					} else {
						sender.sendMessage(ChatColor.RED + "Cannot add!");
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
		if (!hasPermission(sender)) return new ArrayList<String>();
		if (args.size() == 0) return Arrays.asList(new String[] {"add", "get", "set", "gui"});
		if (args.get(0).equalsIgnoreCase("gui")) return new ArrayList<String>();
		
		if (args.size() == 1 || args.size() == 2) {
			String path = "";
			if (args.size() >= 1) path = args.get(1);
			if (path.endsWith(".")) path = path.substring(0, path.length() - 1);
			if (this.config.get().isConfigurationSection(path)) {
				ConfigurationSection section = this.config.get().getConfigurationSection(path);
				List<String> list = new ArrayList<String>();
				list.addAll(section.getKeys(false));
				return list;
			} else {
				List<String> list = new ArrayList<String>();
				list.addAll(this.config.get().getKeys(false));
				return list;
			}
		} else if (args.size() == 3) {
			if (this.config.get().contains(args.get(1))) {
				Object o = this.config.get().get(args.get(1));
				if (!(o instanceof List))
				return Arrays.asList(new String[] {this.config.get().get(args.get(1)).toString()});
			}
		}
		
		
		
		return super.getTabCompletion(sender, args);
	}

}
